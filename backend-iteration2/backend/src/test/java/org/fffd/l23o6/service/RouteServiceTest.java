package org.fffd.l23o6.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RouteServiceTest {

    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    @Test
    void test(){
        List<Long> stationIds = new ArrayList<>();
        stationIds.add(stationService.getStation("南京").getId());
        stationIds.add(stationService.getStation("上海").getId());
        routeService.addRoute("GX-9900", stationIds);

        Long id = routeService.getRoute("GX-9900").getId();
        System.out.println(routeService.getRoute(id).getName());
        System.out.println(routeService.listRoutes());
        stationIds.add(stationService.getStation("北京").getId());
        routeService.editRoute(id, "GX-9901", stationIds);

        routeService.deleteRoute(id);
    }

}
