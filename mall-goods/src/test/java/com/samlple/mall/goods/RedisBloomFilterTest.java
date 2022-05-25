package com.samlple.mall.goods;


import com.sample.mall.goods.MallGoodsApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MallGoodsApplication.class})
public class RedisBloomFilterTest {

    private int size = 10000;
    private static final String BLOOM_FILTER_NAME = "goodsBloomFilter";

//    public void setRedisTemplate(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    @Autowired
    public RedisTemplate redisTemplate;

    private static final Logger logger = Logger.getLogger(RedisBloomFilterTest.class.toString());

    @Before
    public void init() {
        for (int i=0; i<size; i++) {
            redisTemplate.opsForValue().setBit(BLOOM_FILTER_NAME, getOffset(i), true);
        }
    }

    @Test
    public void testRedisFilter() {
        int count = 0;
        for (int j=0; j<size + size; j++) {
            if (redisTemplate.opsForValue().getBit(BLOOM_FILTER_NAME, getOffset(j)) == true) {
                count++;
                System.out.println("误判了" + j + ", 这是第" + count + "次误判");
            }
        }
    }

    private long getOffset(int i) {
        long abs = Math.abs((BLOOM_FILTER_NAME + i).hashCode());
        return (long) (abs % Math.pow(2, 32));
    }
}
