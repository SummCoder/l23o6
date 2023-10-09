package org.fffd.l23o6.service;

import org.fffd.l23o6.pojo.enum_.TrainType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class TrainServiceTest {

    @Autowired
    private TrainService trainService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    @Test
    void test() throws ParseException {
        List<Date> arrivalTimes = new ArrayList<>();
        List<Date> departureTimes = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        arrivalTimes.add(df.parse("2023-07-10 06:00:00"));
        arrivalTimes.add(df.parse("2023-07-10 06:30:00"));
        arrivalTimes.add(df.parse("2023-07-10 07:00:00"));

        departureTimes.add(df.parse("2023-07-10 06:00:00"));
        departureTimes.add(df.parse("2023-07-10 06:35:00"));
        departureTimes.add(df.parse("2023-07-10 07:00:00"));

        trainService.addTrain("GNT-0000",
                routeService.getRoute("南京 - 上海").getId(),
                TrainType.HIGH_SPEED,
                "2023-07-10",
                arrivalTimes,
                departureTimes);

        Long id = trainService.getTrain("GNT-0000").getId();

        System.out.println(trainService.listTrains(
                stationService.getStation("南京").getId(),
                stationService.getStation("上海").getId(),
                "2023-07-10"));

        trainService.deleteTrain(id);
    }

}
