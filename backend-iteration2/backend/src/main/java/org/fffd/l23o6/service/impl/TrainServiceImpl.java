package org.fffd.l23o6.service.impl;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.mapper.RouteMapper;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    @Override
    public TrainDetailVO getTrain(Long trainId) {
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();

        return TrainDetailVO.builder().id(trainId).date(train.getDate()).name(train.getName())
                .stationIds(route.getStationIds()).arrivalTimes(train.getArrivalTimes())
                .departureTimes(train.getDepartureTimes()).extraInfos(train.getExtraInfos()).build();
    }

    @Override
    public TrainVO getTrain(String name){
        return TrainMapper.INSTANCE.toTrainVO(trainDao.findByName(name));
    }

    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        // First, get all routes contains [startCity, endCity]
        // Then, Get all trains on that day with the wanted routes
        List<RouteVO> list = routeDao.findAll().stream().map(RouteMapper.INSTANCE::toRouteVO).collect(Collectors.toList());
        list.removeIf(routeVO -> !checkRoute(startStationId.intValue(), endStationId.intValue(), routeVO.getStationIds()));
        List<AdminTrainVO> trains = trainDao.findAll().stream().map(TrainMapper.INSTANCE::toAdminTrainVO).toList();
        List<TrainVO> res = new ArrayList<>();
        for(RouteVO routeVO: list){
            for(AdminTrainVO train: trains){
                if(Objects.equals(train.getRouteId(), routeVO.getId()) && Objects.equals(train.getDate(), date)){
                    Date departureTime = null;
                    Date arrivalTime = null;
                    for (int i = 0; i < routeVO.getStationIds().size(); i++) {
                        if(routeVO.getStationIds().get(i) == startStationId.intValue()){
                            departureTime = train.getDepartureTimes().get(i);
                        }
                        if(routeVO.getStationIds().get(i) == endStationId.intValue()){
                            arrivalTime = train.getArrivalTimes().get(i);
                            break;
                        }
                    }
                    TrainEntity trainEntity = trainDao.findById(train.getId()).get();
                    List<TicketInfo> ticketInfo = new ArrayList<>();
                    switch (trainEntity.getTrainType()) {
                        case HIGH_SPEED:
                            Map<GSeriesSeatStrategy.GSeriesSeatType, Integer> map = GSeriesSeatStrategy.INSTANCE.getLeftSeatCount(routeVO.getStationIds().indexOf(startStationId.intValue()), routeVO.getStationIds().indexOf(endStationId.intValue()), trainEntity.getSeats());
                            Set<GSeriesSeatStrategy.GSeriesSeatType> sets = map.keySet();
                            for (GSeriesSeatStrategy.GSeriesSeatType set : sets){
                                if(Objects.equals(set.toString(), "BUSINESS_SEAT")){
                                    ticketInfo.add(new TicketInfo(set.getText(), map.get(set), 300));
                                } else if (Objects.equals(set.toString(), "FIRST_CLASS_SEAT")) {
                                    ticketInfo.add(new TicketInfo(set.getText(), map.get(set), 200));
                                } else if (Objects.equals(set.toString(), "SECOND_CLASS_SEAT")) {
                                    ticketInfo.add(new TicketInfo(set.getText(), map.get(set), 100));
                                } else {
                                    ticketInfo.add(new TicketInfo(set.getText(), map.get(set), 100));
                                }
                            }
                            break;
                        case NORMAL_SPEED:
                            Map<KSeriesSeatStrategy.KSeriesSeatType, Integer> map1 = KSeriesSeatStrategy.INSTANCE.getLeftSeatCount(routeVO.getStationIds().indexOf(startStationId.intValue()), routeVO.getStationIds().indexOf(endStationId.intValue()), trainEntity.getSeats());
                            Set<KSeriesSeatStrategy.KSeriesSeatType> sets1 = map1.keySet();
                            for (KSeriesSeatStrategy.KSeriesSeatType set : sets1){
                                if(Objects.equals(set.toString(), "SOFT_SLEEPER_SEAT")){
                                    ticketInfo.add(new TicketInfo(set.getText(), map1.get(set), 400));
                                } else if (Objects.equals(set.toString(), "HARD_SLEEPER_SEAT")) {
                                    ticketInfo.add(new TicketInfo(set.getText(), map1.get(set), 300));
                                } else if (Objects.equals(set.toString(), "SOFT_SEAT")) {
                                    ticketInfo.add(new TicketInfo(set.getText(), map1.get(set), 200));
                                }else if(Objects.equals(set.toString(), "HARD_SEAT")){
                                    ticketInfo.add(new TicketInfo(set.getText(), map1.get(set), 100));
                                }
                            }
                            break;
                    }
                    res.add(TrainVO.builder().id(train.getId()).name(train.getName()).trainType(train.getTrainType())
                            .startStationId(startStationId).endStationId(endStationId)
                            .departureTime(departureTime).arrivalTime(arrivalTime).ticketInfo(ticketInfo).build());
                }
            }
        }
        return res;
    }

    //judge whether the route contains startCity and endCity
    private boolean checkRoute(int start, int end, List<Integer> list){
        boolean res = false;
        for(int i: list){
            if(!res){
                if(i == start){
                    res = true;
                }
            } else {
                if(i == end){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TrainMapper.INSTANCE::toAdminTrainVO).collect(Collectors.toList());
    }

    @Override
    public void addTrain(String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
            List<Date> departureTimes) {
        //调整时间设置
        Date nowTime = new Date(System.currentTimeMillis());
        for (int i = 0; i < departureTimes.size() - 1; i ++) {
            if ((departureTimes.get(i).before(nowTime))
                    || (departureTimes.get(i).before(arrivalTimes.get(i)))
                    || (departureTimes.get(i).after(arrivalTimes.get(i + 1))))
                throw new BizException(BizError.TRAIN_DEPARTURE_TIME_WRONG);
        }

        TrainEntity entity = TrainEntity.builder().name(name).routeId(routeId).trainType(type)
                .date(date).arrivalTimes(arrivalTimes).departureTimes(departureTimes).build();
        RouteEntity route = routeDao.findById(routeId).get();
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }
        trainDao.save(entity);
    }

    @Override
    public void changeTrain(Long id, String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                            List<Date> departureTimes) {
        TrainEntity entity = trainDao.findById(id).get();
        entity.setName(name);
        entity.setRouteId(routeId);
        entity.setTrainType(type);
        entity.setDate(date);
        entity.setArrivalTimes(arrivalTimes);
        entity.setDepartureTimes(departureTimes);
        RouteEntity route = routeDao.findById(routeId).get();
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }
        trainDao.save(entity);
    }

    @Override
    public void deleteTrain(Long id) {
        trainDao.deleteById(id);
    }
}
