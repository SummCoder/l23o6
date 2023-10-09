package org.fffd.l23o6.util.strategy.payment;

public class WechatStrategy extends PaymentStrategy{
    private static final int port = 7777;

    @Override
    public String getName() {
        return "Wechat";
    }

    @Override
    public boolean pay(double number){
        return super.pay(number, port);
    }

}
