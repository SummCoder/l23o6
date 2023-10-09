package org.fffd.l23o6.util.strategy.payment;

public class AlipayStrategy extends PaymentStrategy {

    private static final int port = 6666;
    @Override
    public String getName() {
        return "Alipay";
    }

    @Override
    public boolean pay(double number){
        return super.pay(number, port);
    }
}
