package com.sample.mall.goods.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Aspect
public class MyCacheableAspect {
    private static final Logger logger = LoggerFactory.getLogger(MyCacheableAspect.class);

    @Resource
    private RedisTemplate redisTemplate;

//    @Pointcut("@annotation(com.sample.mall.goods.aspect.MyCacheable)")
//    public void pointCut() {}

    @Around("@annotation(myCacheable)")
    public Object doAroundCache(ProceedingJoinPoint joinPoint, MyCacheable myCacheable) throws Throwable {
        String cacheKey = getCacheKey(joinPoint, myCacheable);

        Object o = redisTemplate.opsForValue().get(cacheKey);
        if (o != null) {
            logger.info("key: {}, value: {}", cacheKey, o);
            return o;
        }
        //  原有方法的运行
        Object result = joinPoint.proceed();

        if (myCacheable.expireInSeconds() <= 0) {
            redisTemplate.opsForValue().set(cacheKey, result);
        } else {
            redisTemplate.opsForValue().set(cacheKey, result, myCacheable.expireInSeconds());
        }
        return result;
    }

    private String getCacheKey(ProceedingJoinPoint joinPoint, MyCacheable myCacheable) {
        String cacheName = myCacheable.cacheName();
        String cacheKey = null;
        String key = myCacheable.key();

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(key);
        StandardEvaluationContext contex = new StandardEvaluationContext();
        // 切面里动态获取参数名和值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 参数列表工具类
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        // 参数名
        String[] params = discoverer.getParameterNames(signature.getMethod());
        // 实际值
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < params.length; i++) {
            contex.setVariable(params[i], args[i]);
        }
        // 最终的key
        cacheKey = cacheName + expression.getValue(contex).toString();
        return cacheKey;
    }
}
