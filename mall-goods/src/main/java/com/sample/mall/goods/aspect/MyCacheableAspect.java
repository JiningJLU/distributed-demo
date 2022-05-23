package com.sample.mall.goods.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Aspect
public class MyCacheableAspect {
    private static final Logger logger = LoggerFactory.getLogger(MyCacheableAspect.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Pointcut("@annotation(com.sample.mall.goods.aspect.MyCacheable)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object doAroundCache(ProceedingJoinPoint joinPoint) {
        return null;
    }
}
