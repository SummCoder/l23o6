package org.fffd.l23o6.data;

import org.fffd.l23o6.mapper.RouteMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteMapperTest {

    @Test
    public void testRouteMapper(){
        RouteVO expected = new RouteVO();
        expected.setName("南京-北京");
        expected.setStationIds(null);
        expected.setId(1L);
        RouteVO actual = RouteMapper.INSTANCE.toRouteVO(new RouteEntity(1L, "南京-北京", null, null, null));
        assertEquals(expected, actual);
    }
}
