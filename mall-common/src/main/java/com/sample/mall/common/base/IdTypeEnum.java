package com.sample.mall.common.base;

public enum IdTypeEnum {
    USER(1, "USER-ID"),
    GOODS(2, "GOODS-ID"),
    ORDER(3, "ORDER-ID"),
    COUPON(4, "COUPON-ID");

    private int code;
    private String redisCounter;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRedisCounter() {
        return redisCounter;
    }

    public void setRedisCounter(String redisCounter) {
        this.redisCounter = redisCounter;
    }

    IdTypeEnum(int code, String redisCounter) {
        this.code = code;
        this.redisCounter = redisCounter;
    }
}
