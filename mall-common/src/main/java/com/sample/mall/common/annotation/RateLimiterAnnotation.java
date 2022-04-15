package com.sample.mall.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义限流相关的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiterAnnotation {

    /**
     * 每秒生成的令牌数
     *
     * @return
     */
    double permitsPerSecond();

    /**
     * 是否预热
     *
     * @return
     */
    boolean isWarmup() default false;

    /**
     * 预热时间
     *
     * @return
     */
    long warmupPeriod() default 3;

    /**
     * 预热时间单位
     *
     * @return
     */
    TimeUnit warmupTimeUnit() default TimeUnit.SECONDS;

    /**
     * 定义一个获取多个令牌
     *
     * @return
     */
    int permits() default 1;

    /**
     * 获取令牌的最大等待时间
     *
     * @return
     */
    long timeout();

    /**
     * 获取令牌最大等待时间的单位，这里默认是毫秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 如果触发限流时，返回的内容
     *
     * @return
     */
    String msg() default "当前服务繁忙，请稍后再试！";
}
