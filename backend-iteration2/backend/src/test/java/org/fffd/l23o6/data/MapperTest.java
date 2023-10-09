package org.fffd.l23o6.data;

import org.junit.jupiter.api.Test;

public class MapperTest {
    @Test
    void MapperTest01(){
        OrderMapperTest orderMapperTest = new OrderMapperTest();
        orderMapperTest.testOrderMapper();
    }

    @Test
    void MapperTest02(){
        RouteMapperTest routeMapperTest = new RouteMapperTest();
        routeMapperTest.testRouteMapper();
    }

    @Test
    void MapperTest03(){
        StationMapperTest stationMapperTest = new StationMapperTest();
        stationMapperTest.testStationMapper();
    }

    @Test
    void MapperTest04(){
        TrainMapperTest trainMapperTest = new TrainMapperTest();
        trainMapperTest.testTrainMapper();
    }

    @Test
    void MapperTest05(){
        UserMapperTest userMapperTest = new UserMapperTest();
        userMapperTest.testUserMapper();
    }
}
