package org.fffd.l23o6.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Test
    void testAddStation(){
        stationService.addStation("上海");
    }

    @Test
    void testEditStation(){
        stationService.addStation("海门市");
        stationService.editStation(stationService.getStation("海门市").getId(), "海门");
    }

    @Test
    void testDeleteStation(){
        stationService.addStation("火星");
        stationService.deleteStation(stationService.getStation("火星").getId());
    }

    @Test
    void testListStations(){
        System.out.println(stationService.listStations());
    }

    @Test
    void testGetStation(){
        System.out.println(stationService.getStation("上海").getId());
    }

    @Test
    void testGetStationById(){
        System.out.println(stationService.getStation(stationService.getStation("上海").getId()).getName());
    }

}
