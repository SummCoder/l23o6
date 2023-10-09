package org.fffd.l23o6.data;

import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainMapperTest {
    @Test
    public void testTrainMapper(){
        TrainVO expected = new TrainVO();
        expected.setId(1L);
        expected.setName("C3175");
        expected.setTrainType("HIGH_SPEED");
        TrainVO actual = TrainMapper.INSTANCE.toTrainVO(new TrainEntity(1L, "C3175", null, null, TrainType.HIGH_SPEED, null, null, null, null, null, null));
        assertEquals(expected, actual);
    }
}
