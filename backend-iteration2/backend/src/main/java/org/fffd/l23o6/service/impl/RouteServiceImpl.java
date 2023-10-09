package org.fffd.l23o6.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.mapper.RouteMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.RouteService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final RouteDao routeDao;
    private final TrainDao trainDao;
    @Override
    public void addRoute(String name, List<Long> stationIds) {
        RouteEntity route = RouteEntity.builder().name(name).stationIds(stationIds).build();
        routeDao.save(route);
    }

    @Override
    public List<RouteVO> listRoutes() {
        return routeDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(RouteMapper.INSTANCE::toRouteVO).collect(Collectors.toList());
    }

    @Override
    public RouteVO getRoute(Long id) {
        RouteEntity entity = routeDao.findById(id).get();
        return RouteMapper.INSTANCE.toRouteVO(entity);
    }

    @Override
    public RouteVO getRoute(String name){
        return RouteMapper.INSTANCE.toRouteVO(routeDao.findByName(name));
    }

    @Override
    public void editRoute(Long id, String name, List<Long> stationIds) {
        routeDao.save(routeDao.findById(id).get().setStationIds(stationIds).setName(name));
    }

    @Override
    public void deleteRoute(Long id) {
        List<TrainEntity> trains = trainDao.findAll();
        for (int i = 0; i < trains.size(); i ++) {
            if (id.equals(trains.get(i).getRouteId())) throw new BizException(BizError.ROUTE_IN_TRAIN);
        }
        routeDao.deleteById(id);
    }
}
