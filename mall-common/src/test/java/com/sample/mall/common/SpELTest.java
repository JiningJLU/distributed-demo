package com.sample.mall.common;

import com.sample.mall.common.util.Assert;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Objects;

public class SpELTest {

    @Test
    public void testStringFormat() {
        String goodsCacheKey = "mall:goods:%s";
        String cacheKey = String.format(goodsCacheKey, 1);
        Assert.assertEquals(cacheKey, "mall:goods:1");
    }

    @Test
    public void testSpEl() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("'mall:goods:' + #userId" );
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("userId", 1);
        String s = Objects.requireNonNull(expression.getValue(context)).toString();
        Assert.assertEquals(s, "mall:goods:1");
    }


}
