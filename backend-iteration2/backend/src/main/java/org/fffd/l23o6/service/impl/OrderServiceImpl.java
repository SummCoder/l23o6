package org.fffd.l23o6.service.impl;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import jakarta.validation.constraints.NotNull;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.pojo.enum_.PaymentType;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.payment.AlipayStrategy;
import org.fffd.l23o6.util.strategy.payment.PaymentContext;
import org.fffd.l23o6.util.strategy.payment.PaymentStrategy;
import org.fffd.l23o6.util.strategy.payment.WechatStrategy;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;


    private final static long[] level = new long[]{0, 1000, 3000, 10000, 50000};
    private final static long[] discount = new long[]{0, 1, 3, 14, 100};
    private final static double[] rate = new double[]{0.001, 0.0015, 0.002, 0.0025, 0.003};


    public String[] gSeriesSeats = {"1车1A","1车1C","1车1F"
            , "2车1A","2车1C","2车1D","2车1F","2车2A","2车2C","2车2D","2车2F","3车1A","3车1C","3车1D","3车1F"
            , "4车1A","4车1B","4车1C","4车1D","4车2F","4车2A","4车2B","4车2C","4车2D","4车2F","4车3A","4车3B","4车3C","4车3D","4车3F"};
    public String[] kSeriesSeats = {"软卧1号上铺", "软卧2号下铺", "软卧3号上铺", "软卧4号上铺", "软卧5号上铺", "软卧6号下铺", "软卧7号上铺", "软卧8号上铺"
            , "硬卧1号上铺", "硬卧2号中铺", "硬卧3号下铺", "硬卧4号上铺", "硬卧5号中铺", "硬卧6号下铺", "硬卧7号上铺", "硬卧8号中铺", "硬卧9号下铺", "硬卧10号上铺", "硬卧11号中铺", "硬卧12号下铺"
            , "1车1座", "1车2座", "1车3座", "1车4座", "1车5座", "1车6座", "1车7座", "1车8座", "2车1座", "2车2座", "2车3座", "2车4座", "2车5座", "2车6座", "2车7座", "2车8座"
            , "3车1座", "3车2座", "3车3座", "3车4座", "3车5座", "3车6座", "3车7座", "3车8座", "3车9座", "3车10座", "4车1座", "4车2座", "4车3座", "4车4座", "4车5座", "4车6座", "4车7座", "4车8座", "4车9座", "4车10座"};


    public Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType,
            Long seatNumber, boolean whether_use_points) {
        Long userId = userDao.findByUsername(username).getId();
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);

        // 不可购买已经过期的车次
        Date departureTime = train.getDepartureTimes().get(startStationIndex);
        if (departureTime.before(new Date(System.currentTimeMillis()))) throw new BizException(BizError.TIME_UP);

        String seat = null;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seat = GSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType), train.getSeats());
                break;
            case NORMAL_SPEED:
                seat = KSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType), train.getSeats());
                break;
        }
        if (seat == null) {
            throw new BizException(BizError.OUT_OF_SEAT);
        }
        double amount = 0;
        UserEntity user = userDao.findByUsername(username);
//        增加amount属性用以存储金额
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                if(GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType) == GSeriesSeatStrategy.GSeriesSeatType.BUSINESS_SEAT){
                    amount = 300;
                } else if (GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType) == GSeriesSeatStrategy.GSeriesSeatType.FIRST_CLASS_SEAT) {
                    amount = 200;
                }else {
                    amount = 100;
                }
                break;
            case NORMAL_SPEED:
                if(KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType) == KSeriesSeatStrategy.KSeriesSeatType.SOFT_SLEEPER_SEAT){
                    amount = 400;
                } else if (KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType) == KSeriesSeatStrategy.KSeriesSeatType.HARD_SLEEPER_SEAT) {
                    amount = 300;
                } else if (KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType) == KSeriesSeatStrategy.KSeriesSeatType.SOFT_SEAT) {
                    amount = 200;
                }else {
                    amount = 100;
                }
                break;
        }

        double originAmount = amount;

        Pair<Double, Long> res = handleOrder(user.getPoints(), amount, whether_use_points);

        amount = res.component1();
        long remainPoints = user.getPoints() - res.component2();

        if (remainPoints > 0) {
            user.setPoints(res.component2());
            userDao.save(user);
        }

        OrderEntity order = OrderEntity.builder().trainId(trainId).userId(userId).seat(seat)
                .status(OrderStatus.PENDING_PAYMENT).arrivalStationId(toStationId).departureStationId(fromStationId)
                .whether_use_points(whether_use_points).paymentType("支付后显示").amount(amount).originAmount(originAmount).remainPoints(remainPoints)
                .build();
        train.setUpdatedAt(null);// force it to update

        trainDao.save(train);
        orderDao.save(order);
        return order.getId();
    }

    public List<OrderVO> listOrders(String username) {
        Long userId = userDao.findByUsername(username).getId();
        UserEntity user = userDao.findByUsername(username);
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1,o2)-> o2.getId().compareTo(o1.getId()));
        return orders.stream().map(order -> {
            TrainEntity train = trainDao.findById(order.getTrainId()).get();
            RouteEntity route = routeDao.findById(train.getRouteId()).get();
            int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
            Date departureTime = train.getDepartureTimes().get(startIndex);
            Date arrivalTime = train.getArrivalTimes().get(endIndex);

            //根据时间调整订单状态
            if (departureTime.before(new Date(System.currentTimeMillis()))) {
                if (order.getStatus().equals(OrderStatus.PENDING_PAYMENT)) {
                    order.setStatus(OrderStatus.CANCELLED); //不退还积分
//                    不退还积分的含义是未按时支付的惩罚措施？还是只是不增加积分？
//                    姑且认为是后者，哦，想来应是前者
//                    毕竟如果使用了积分，又没有进行cancel，那部分积分肯定就白搭了
//                    可是似乎我就不使用积分，也是占着票不退，似乎没有任何影响，是否可以考虑为归还积分但要扣除一定数额的积分？
                    order.setAmount(0);//将支付金额置为0
                } else if(order.getStatus().equals(OrderStatus.PAID)) {
                    order.setStatus(OrderStatus.COMPLETED);

                    //增加积分
                    user.setPoints(user.getPoints() + (long)order.getOriginAmount());
                    userDao.save(user);
                    order.setRemainPoints(order.getRemainPoints() + (long)order.getOriginAmount());
                }
                orderDao.save(order);
            }

            return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                    .seat(order.getSeat()).status(order.getStatus().getText())
                    .createdAt(order.getCreatedAt())
                    .startStationId(order.getDepartureStationId())
                    .endStationId(order.getArrivalStationId())
                    .departureTime(departureTime)
                    .arrivalTime(arrivalTime)
                    .amount(order.getAmount())
                    .originAmount(order.getOriginAmount())
                    .remainPoints(-order.getRemainPoints())
                    .build();
        }).collect(Collectors.toList());
    }

    public OrderVO getOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        UserEntity user = userDao.findById(order.getUserId()).get();
        int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());

        Date departureTime = train.getDepartureTimes().get(startIndex);
        Date arrivalTime = train.getArrivalTimes().get(endIndex);

        if (departureTime.before(new Date(System.currentTimeMillis()))) {
            if (order.getStatus().equals(OrderStatus.PENDING_PAYMENT)) {
                order.setStatus(OrderStatus.CANCELLED); //不退还积分
                order.setAmount(0);
                //将支付金额置为0，可能发现存在已取消但未将金额置为0的情况，那是之前数据未在取消操作后执行该操作
                //原本我的做法是get之后如果状态为0就将该值置为0，可想来，数据的变化未与实际相关动作联系起来
            } else if(order.getStatus().equals(OrderStatus.PAID)) {
                order.setStatus(OrderStatus.COMPLETED);

                //增加积分
                user.setPoints(user.getPoints() + (long)order.getOriginAmount());
                userDao.save(user);
                order.setRemainPoints(order.getRemainPoints() + (long)order.getOriginAmount());
            }
            orderDao.save(order);
        }
        // 用于可能的积分变化展示
        return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                .seat(order.getSeat()).status(order.getStatus().getText())
                .createdAt(order.getCreatedAt())
                .startStationId(order.getDepartureStationId())
                .endStationId(order.getArrivalStationId())
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .amount(order.getAmount())
                .originAmount(order.getOriginAmount())
                .remainPoints(-order.getRemainPoints())
                .paymentType(order.getPaymentType())
                .build();
    }

    public boolean cancelOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }
        //等待支付状态取消，将取出的票还原，已支付状态，不仅要将取出的票还原，还要返还用户
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endStationIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        int index = 0;

        //还原车票
        @NotNull boolean[][] seats = train.getSeats();
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                for (int i = 0; i < gSeriesSeats.length; i++) {
                    if(Objects.equals(order.getSeat(), gSeriesSeats[i])){
                        index = i;
                        break;
                    }
                }
                for (int i = startStationIndex; i < endStationIndex; i++) {
                    seats[i][index] = false;
                }
                break;
            case NORMAL_SPEED:
                for (int i = 0; i < kSeriesSeats.length; i++) {
                    if(Objects.equals(order.getSeat(), kSeriesSeats[i])){
                        index = i;
                        break;
                    }
                }
                for (int i = startStationIndex; i < endStationIndex; i++) {
                    seats[i][index] = false;
                }
                break;
        }
        train.setSeats(seats);

        // 对用户账号进行退款
        UserEntity user = userDao.findById(order.getUserId()).get();
        if(order.getStatus() == OrderStatus.PAID){
            //退款
            PaymentContext paymentContext = new PaymentContext(order.getPaymentType());
            if (!paymentContext.pay(-order.getAmount())) return false;
        }

        //恢复积分...
        user.setPoints(user.getPoints() + order.getRemainPoints());
        userDao.save(user);

        //更新状态
        order.setStatus(OrderStatus.CANCELLED);
        order.setAmount(0);//设置已支付金额为0
        order.setRemainPoints(0L);//设置积分变化为0
        orderDao.save(order);

        train.setUpdatedAt(null);// force it to update
        trainDao.save(train);

        return true;
    }

    public boolean payOrder(Long id, String paymentType) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {//等待支付状态
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        // 支付
        PaymentContext paymentContext = new PaymentContext(paymentType);
        if(!paymentContext.pay(order.getAmount())) return false;


        //更新订单状态
        order.setPaymentType(paymentType);
        order.setStatus(OrderStatus.PAID);
        orderDao.save(order);

        return true;
    }


    //利用表驱动获取当前积分所对应等级
    private int getLevel(Long mileagePoints) {
        int Level = 4;
        for (int i = 4; i > 0; i --) {
            if (mileagePoints < level[i]) Level = i - 1;
            else break;
        }
        return Level;
    }

    /**
     * 处理价格
     * @param mileagePoints :积分
     * @param num           :原价
     * @param pointsFlag    :是否使用积分
     * @return :返回处理后的价格和积分
     */
    private Pair<Double, Long> handleOrder(Long mileagePoints, double num, boolean pointsFlag) {
        Pair<Double, Long> res;
        if (pointsFlag) {
            int Level = getLevel(mileagePoints);
            num -= (mileagePoints - level[Level]) * rate[Level];
            if (num <= 0) { //无需降级打折
                mileagePoints = (level[Level] - (long) (num / rate[Level]));
                num = 0;
            } else { //降级打折
                mileagePoints = level[Level];
                for (int i = Level; i >= 1; i --) {
                    if (num < discount[i]) {
                        mileagePoints -= (long)(num * rate[i - 1]);
                        num = 0;
                        break;
                    }
                    num -= discount[i];
                    mileagePoints = level[i - 1];
                }
            }
        }
        res = new Pair<>(num, mileagePoints);
        return res;
    }
}
