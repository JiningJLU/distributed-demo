package com.sample.mall.goods.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于在方法执行之前判断缓存是否存在，如果存在则返回，不存在则先查db，后设置缓存。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyCacheable {
    // key prefix, rule is {cacheName} : {realKey}
    String cacheName();

    // realKey, 可以用El表达式，可以匹配参数值
    String key();

    int expireInSeconds() default 0;
}
