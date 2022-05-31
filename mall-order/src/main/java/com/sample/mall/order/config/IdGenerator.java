package com.sample.mall.order.config;

import com.sample.mall.common.base.IdTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class IdGenerator {
    // 1位业务编码 + 15 时间 + 3序列号

    @Resource
    private RedisTemplate redisTemplate;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

    public Long incr(IdTypeEnum idTypeEnum) {
        String dateTime = dateTimeFormatter.format(LocalDateTime.now());
        // key - value -> key - value + 1
        Long increment = redisTemplate.opsForValue().increment(idTypeEnum.getRedisCounter(), 1);
        increment = increment >= 1000 ? increment % 1000 : 1000;
        // 19 -> 019,  1 -> 001
        String finalStr = StringUtils.leftPad(String.valueOf(increment), 3, "0");
        String res = idTypeEnum.getCode() + dateTime + finalStr;
        return Long.parseLong(res);
    }
}
