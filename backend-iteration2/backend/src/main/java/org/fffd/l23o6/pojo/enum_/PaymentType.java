package org.fffd.l23o6.pojo.enum_;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PaymentType {
    @JsonProperty("支付宝支付") ALIPAY("支付宝支付"), @JsonProperty("微信支付") WECHAT("微信支付");
    private String text;

    PaymentType(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
