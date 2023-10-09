package org.fffd.l23o6.data;

import org.fffd.l23o6.mapper.UserMapper;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.vo.user.UserVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    @Test
    public void testUserMapper(){
        UserVO expectedUser = new UserVO();
        expectedUser.setName("李白");
        expectedUser.setUsername("李白");
        expectedUser.setPhone("13916252689");
        expectedUser.setIdn("320625200306300000");
        expectedUser.setType("身份证");
        expectedUser.setPoints(1000L);
        UserVO actualUser = UserMapper.INSTANCE.toUserVO(new UserEntity(3L, "李白", "Libai114514", "李白", "13916252689", "身份证", "320625200306300000", null, null, 1000L));
        assertEquals(expectedUser, actualUser);
    }
}
