package com.sample.mall.coupon.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.sample.mall.common.annotation.RateLimiterAnnotation;
import com.sample.mall.common.base.BaseResponse;
import com.sample.mall.common.util.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定义一个切面，用于实现限流
 */
@Aspect
@Component
public class RateLimiterAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    /**
     * 以资源为key值，存储多个的限流器
     */
    private Map<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    /**
     * 创建切面，具体实现限流逻辑
     */
    @Pointcut("execution(public * com.sample.mall.*.controller.*.*(..))")
    public void limiting() { }

    @Around("limiting()")
    public Object process(ProceedingJoinPoint point) throws Throwable {

        log.info("{}开始进行限流处理...", point.getSignature().getName());

        // 从当前目标的连接点，获取方法前面，进一步获取 Controller 上的 RateLimiterAnnotation
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        RateLimiterAnnotation rateLimiterAnnotation = methodSignature.getMethod().getAnnotation(RateLimiterAnnotation.class);
        if (rateLimiterAnnotation != null) {

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String resource = request.getRequestURI();

            // 容器中没有对应资源的限流器，先创建一个限流器
            if (!limiters.containsKey(resource)) {
                limiters.put(resource, this.createRateLimiter(rateLimiterAnnotation, resource));
            }

            RateLimiter rateLimiter = limiters.get(resource);
            boolean acquired = rateLimiter.tryAcquire(
                    rateLimiterAnnotation.permits(),
                    rateLimiterAnnotation.timeout(),
                    rateLimiterAnnotation.timeUnit()
            );
            
            if (!acquired) {
                // 没有获取到令牌需要限流处理
                log.warn("{}触发限流限制了！", point.getSignature().getName());
                this.printResponse(rateLimiterAnnotation);
            }
        }

        return point.proceed();
    }

    private RateLimiter createRateLimiter (RateLimiterAnnotation rateLimiterAnnotation, String resource) {
        // 新建限流器时需要通过锁来创建，不然高并发时可能在极短时间内，后边的限流器会覆盖前面的限流器
        synchronized (resource) {
            if (rateLimiterAnnotation.isWarmup()) {
                // 以平滑预热方法创建限流器
                return RateLimiter.create(
                        rateLimiterAnnotation.permitsPerSecond(),
                        rateLimiterAnnotation.warmupPeriod(),
                        rateLimiterAnnotation.warmupTimeUnit()
                );
            } else {
                return RateLimiter.create(rateLimiterAnnotation.permitsPerSecond());
            }
        }
    }

    private void printResponse (RateLimiterAnnotation rateLimiterAnnotation) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode("8888");
            baseResponse.setMessage(rateLimiterAnnotation.msg());
            writer.printf(JSONUtil.toJSONString(baseResponse));
        } catch (Exception e) {
            log.error("操作PrintWriter异常", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
