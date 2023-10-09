package org.fffd.l23o6.pojo.vo.order;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class OrderVO {
    private Long id;
    private Long trainId;
    private Long startStationId;
    private Long endStationId;
    private Date departureTime;
    private Date arrivalTime;
    private String status;
    private Date createdAt;
    private String seat;

    //new
    private boolean whether_use_points;
    private String paymentType;
    private double amount;
    private double originAmount;
    private Long remainPoints;
}
