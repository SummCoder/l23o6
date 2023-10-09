package org.fffd.l23o6.util.strategy.train;

import java.util.*;

import jakarta.annotation.Nullable;


public class GSeriesSeatStrategy extends TrainSeatStrategy {
    public static final GSeriesSeatStrategy INSTANCE = new GSeriesSeatStrategy();
     
    private final Map<Integer, String> BUSINESS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> FIRST_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> SECOND_CLASS_SEAT_MAP = new HashMap<>();

    private final Map<GSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(GSeriesSeatType.BUSINESS_SEAT, BUSINESS_SEAT_MAP);
        put(GSeriesSeatType.FIRST_CLASS_SEAT, FIRST_CLASS_SEAT_MAP);
        put(GSeriesSeatType.SECOND_CLASS_SEAT, SECOND_CLASS_SEAT_MAP);
    }};


    private GSeriesSeatStrategy() {

        int counter = 0;

        for (String s : Arrays.asList("1车1A","1车1C","1车1F")) {
            BUSINESS_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("2车1A","2车1C","2车1D","2车1F","2车2A","2车2C","2车2D","2车2F","3车1A","3车1C","3车1D","3车1F")) {
            FIRST_CLASS_SEAT_MAP.put(counter++, s);
        }

        for (String s : Arrays.asList("4车1A","4车1B","4车1C","4车1D","4车2F","4车2A","4车2B","4车2C","4车2D","4车2F","4车3A","4车3B","4车3C","4车3D","4车3F")) {
            SECOND_CLASS_SEAT_MAP.put(counter++, s);
        }
        
    }


    public enum GSeriesSeatType implements SeatType {
        BUSINESS_SEAT("商务座"), FIRST_CLASS_SEAT("一等座"), SECOND_CLASS_SEAT("二等座"), NO_SEAT("无座");
        private String text;
        GSeriesSeatType(String text){
            this.text=text;
        }
        public String getText() {
            return this.text;
        }
        public static GSeriesSeatType fromString(String text) {
            for (GSeriesSeatType b : GSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, GSeriesSeatType type, boolean[][] seatMap) {
        //endStationIndex - 1 = upper bound
        int colStart = getSeatTypeStartIndex(type);
        int total = 0;
        if(colStart == 0){
            total = BUSINESS_SEAT_MAP.size();
        } else if (colStart == BUSINESS_SEAT_MAP.size()) {
            total = FIRST_CLASS_SEAT_MAP.size();
        } else if (colStart == BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size()) {
            total = SECOND_CLASS_SEAT_MAP.size();
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
    private int getSeatTypeStartIndex(GSeriesSeatType type) {
        switch (type) {
            case BUSINESS_SEAT:
                return 0;
            case FIRST_CLASS_SEAT:
                return BUSINESS_SEAT_MAP.size();
            case SECOND_CLASS_SEAT:
                return BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size();
            default:
                return Integer.MAX_VALUE;
        }
    }
    //判断是哪种类型的车票
    private String getSeatCode(int col) {
        if (col < BUSINESS_SEAT_MAP.size()) {
            return BUSINESS_SEAT_MAP.get(col);
        } else if (col < BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size()) {
            return FIRST_CLASS_SEAT_MAP.get(col);
        } else if (col < BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size() + SECOND_CLASS_SEAT_MAP.size()) {
            return SECOND_CLASS_SEAT_MAP.get(col);
        }
        throw new IllegalArgumentException("Invalid seat column index");
    }

    public Map<GSeriesSeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<GSeriesSeatType, Integer> leftSeatCountMap = new HashMap<>();
        int row = startStationIndex;
        //对于每一种座位类型，检查从起始站到终点站之间的每一行，统计可用的座位数并存储到leftSeatCountMap中
        for (GSeriesSeatType type : GSeriesSeatType.values()) {
            int colStart = getSeatTypeStartIndex(type);
            int leftSeatCount = 0;
            int total = 0;
            if(colStart == 0){
                total = BUSINESS_SEAT_MAP.size();
            } else if (colStart == BUSINESS_SEAT_MAP.size()) {
                total = FIRST_CLASS_SEAT_MAP.size();
            } else if (colStart == BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size()) {
                total = SECOND_CLASS_SEAT_MAP.size();
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

//    票是按照站台和三种作为类型的size确定
    public boolean[][] initSeatMap(int stationCount) {
        return new boolean[stationCount - 1][BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size() + SECOND_CLASS_SEAT_MAP.size()];
    }
}
