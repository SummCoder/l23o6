package org.fffd.l23o6.exception;

import io.github.lyc8503.spring.starter.incantation.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BizError implements ErrorType {

    USERNAME_EXISTS(200001, "用户名已存在", 400),
    INVALID_CREDENTIAL(200002, "用户名或密码错误", 400),
    STATIONNAME_EXISTS(200003, "同名站点已存在", 400),
    OUT_OF_SEAT(300001, "无可用座位", 400),
    ILLEAGAL_ORDER_STATUS(400001, "非法的订单状态", 400),

    //new
    PAY_FAILED(500001, "支付失败", 400),
    CANCEL_FAILED(600001, "取消订单失败", 400),
    STATION_IN_ROUTE(700001, "站点在路线中存在", 400),
    ROUTE_IN_TRAIN(800001, "该路线有车次存在", 400),
    TIME_UP(900001, "火车已启动或是已完成", 400),
    TRAIN_DEPARTURE_TIME_WRONG(1000001, "火车出发时间错误", 400),
    PAYMENT_TYPE_WRONG(1100001, "支付方式错误", 400);

    final int code;
    final String message;
    final int httpCode;
}
