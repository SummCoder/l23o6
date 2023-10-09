package org.fffd.l23o6.data;

import org.fffd.l23o6.mapper.StationMapper;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StationMapperTest {
    @Test
    public void testStationMapper(){
        StationVO expected = new StationVO(1L, "南京站");
        StationVO actual = StationMapper.INSTANCE.toStationVO(new StationEntity(1L, "南京站", null, null));
        assertEquals(expected, actual);
    }
}
