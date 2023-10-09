package org.fffd.l23o6.util.strategy.payment;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import org.fffd.l23o6.exception.BizError;

import java.util.LinkedHashMap;


//支付环境类，用于构造和使用支付类
public class PaymentContext {

    private PaymentStrategy paymentStrategy;

    LinkedHashMap<String, PaymentStrategy> paymentTypes = new LinkedHashMap<>(){{
        put("支付宝支付", new AlipayStrategy());
        put("微信支付", new WechatStrategy());
    }
    };

    public PaymentContext(String name) {
        paymentStrategy = paymentTypes.get(name);
        if (paymentStrategy == null) throw new BizException(BizError.PAYMENT_TYPE_WRONG);
    }

    public boolean pay(Double number) {
        return paymentStrategy.pay(number);
    }
}
