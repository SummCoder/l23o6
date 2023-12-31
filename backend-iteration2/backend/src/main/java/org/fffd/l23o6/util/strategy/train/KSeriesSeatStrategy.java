package org.fffd.l23o6.util.strategy.train;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Nullable;


public class KSeriesSeatStrategy extends TrainSeatStrategy {
    public static final KSeriesSeatStrategy INSTANCE = new KSeriesSeatStrategy();
     
    private final Map<Integer, String> SOFT_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> HARD_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> SOFT_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> HARD_SEAT_MAP = new HashMap<>();

    private final Map<KSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(KSeriesSeatType.SOFT_SLEEPER_SEAT, SOFT_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.HARD_SLEEPER_SEAT, HARD_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.SOFT_SEAT, SOFT_SEAT_MAP);
        put(KSeriesSeatType.HARD_SEAT, HARD_SEAT_MAP);
    }};


    private KSeriesSeatStrategy() {

        int counter = 0;

        for (String s : Arrays.asList("软卧1号上铺", "软卧2号下铺", "软卧3号上铺", "软卧4号上铺", "软卧5号上铺", "软卧6号下铺", "软卧7号上铺", "软卧8号上铺")) {
            SOFT_SLEEPER_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("硬卧1号上铺", "硬卧2号中铺", "硬卧3号下铺", "硬卧4号上铺", "硬卧5号中铺", "硬卧6号下铺", "硬卧7号上铺", "硬卧8号中铺", "硬卧9号下铺", "硬卧10号上铺", "硬卧11号中铺", "硬卧12号下铺")) {
            HARD_SLEEPER_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("1车1座", "1车2座", "1车3座", "1车4座", "1车5座", "1车6座", "1车7座", "1车8座", "2车1座", "2车2座", "2车3座", "2车4座", "2车5座", "2车6座", "2车7座", "2车8座")) {
            SOFT_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("3车1座", "3车2座", "3车3座", "3车4座", "3车5座", "3车6座", "3车7座", "3车8座", "3车9座", "3车10座", "4车1座", "4车2座", "4车3座", "4车4座", "4车5座", "4车6座", "4车7座", "4车8座", "4车9座", "4车10座")) {
            HARD_SEAT_MAP.put(counter++, s);
        }
    }

    public enum KSeriesSeatType implements SeatType {
        SOFT_SLEEPER_SEAT("软卧"), HARD_SLEEPER_SEAT("硬卧"), SOFT_SEAT("软座"), HARD_SEAT("硬座"), NO_SEAT("无座");
        private String text;
        KSeriesSeatType(String text){
            this.text=text;
        }
        public String getText() {
            return this.text;
        }
        public static KSeriesSeatType fromString(String text) {
            for (KSeriesSeatType b : KSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

//  分配座位
    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, KSeriesSeatType type, boolean[][] seatMap) {
        //endStationIndex - 1 = upper bound
        int colStart = getSeatTypeStartIndex(type);
        int total = 0;
        if(colStart == 0){
            total = SOFT_SLEEPER_SEAT_MAP.size();
        } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size()) {
            total = HARD_SLEEPER_SEAT_MAP.size();
        } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size()) {
            total = SOFT_SEAT_MAP.size();
        } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size()) {
            total = HARD_SEAT_MAP.size();
        }
        //通过遍历座位图中的列来查找可用的座位
        for (int col = colStart; col < colStart + total; col++) {
            boolean available = true;
            //对于每一列，检查从起始站到终点站之间的每一行，如果发现某一行的座位已被占用，则将该列标记为不可用
            for (int i = startStationIndex; i < endStationIndex; i++) {
                if (seatMap[i][col]) {
                    available = false;
                    break;
                }
            }
            if (available) {
                for (int i = startStationIndex; i < endStationIndex; i++) {
                    seatMap[i][col] = true;
                }
                return getSeatCode(col);
            }
        }
        return null;
    }

    //根据不同的车票类型得到开始的列index
    private int getSeatTypeStartIndex(KSeriesSeatStrategy.KSeriesSeatType type) {
        switch (type) {
            case SOFT_SLEEPER_SEAT:
                return 0;
            case HARD_SLEEPER_SEAT:
                return SOFT_SLEEPER_SEAT_MAP.size();
            case SOFT_SEAT:
                return SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size();
            case HARD_SEAT:
                return SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size();
            default:
                return Integer.MAX_VALUE;
        }
    }
    //判断是哪种类型的车票
    private String getSeatCode(int col) {
        if (col < SOFT_SLEEPER_SEAT_MAP.size()) {
            return SOFT_SLEEPER_SEAT_MAP.get(col);
        } else if (col < SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size()) {
            return HARD_SLEEPER_SEAT_MAP.get(col);
        } else if (col < SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size()) {
            return SOFT_SEAT_MAP.get(col);
        } else {
            return HARD_SEAT_MAP.get(col);
        }
    }

    public Map<KSeriesSeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<KSeriesSeatStrategy.KSeriesSeatType, Integer> leftSeatCountMap = new HashMap<>();
        int row = startStationIndex;
        //对于每一种座位类型，检查从起始站到终点站之间的每一行，统计可用的座位数并存储到leftSeatCountMap中
        for (KSeriesSeatStrategy.KSeriesSeatType type : KSeriesSeatStrategy.KSeriesSeatType.values()) {
            int colStart = getSeatTypeStartIndex(type);
            int leftSeatCount = 0;
            int total = 0;
            if(colStart == 0){
                total = SOFT_SLEEPER_SEAT_MAP.size();
            } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size()) {
                total = HARD_SLEEPER_SEAT_MAP.size();
            } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size()) {
                total = SOFT_SEAT_MAP.size();
            } else if (colStart == SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size()) {
                total = HARD_SEAT_MAP.size();
            }
            for (int col = colStart; col < colStart + total; col++) {
                boolean available = true;
                for (int i = row; i < endStationIndex; i++) {
                    if (seatMap[i][col]) {
                        available = false;
                        break;
                    }
                }
                if (available) {
                    leftSeatCount++;
                }
            }
            leftSeatCountMap.put(type, leftSeatCount);
        }
        return leftSeatCountMap;
    }

    public boolean[][] initSeatMap(int stationCount) {
        return new boolean[stationCount - 1][SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size() + HARD_SEAT_MAP.size()];
    }
}
