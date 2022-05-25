package com.samlple.mall.goods;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Before;
import org.junit.Test;

public class GuavaBloomFilterTest {
    private int size = 1000;
    private double fpp = 0.001;

    private BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);

    @Before
    public void initBloomFilter() {
        for (int i=0; i<size; i++) {
            bloomFilter.put(i);
        }
    }

    @Test
    public void testBloomFilter() {
        int count = 0;
        for (int j=size; j<size + size; j++) {
            if (bloomFilter.mightContain(j)) {
                count++;
                System.out.println("误判了" + j + ", 这是第" + count + "次误判");
            }
        }
    }
}
