package com.github.xincao9.redis.experiment.controller;

import com.github.xincao9.redis.experiment.GenderEnum;
import com.github.xincao9.redis.experiment.User;
import com.github.xincao9.redis.experiment.UserStatusEnum;
import com.github.xincao9.redis.experiment.util.PbUtils;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("compress")
public class CompressController {

    @Autowired
    private JedisPool jedisPool;

    private void set(String key, byte[] data) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key.getBytes(StandardCharsets.UTF_8), data);
        }
    }

    private void set(String key, Long value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, String.valueOf(value));
        }
    }

    @GetMapping("protobuf")
    public int protobuf() {
        User user = randomUser();
        byte[] data = user.toByteArray();
        int len = data.length;
        write(user, data);
        return len;
    }

    private void write(User user, byte[] data) {
        set(String.format("mobile_%d", user.getMobile().getValue()), data);
        set(String.format("user-id_mobile_map_%d", user.getUserId().getValue()), user.getMobile().getValue());
        set(String.format("user-uuid_mobile_map_%d", user.getUserUuid().getValue()), user.getMobile().getValue());
    }

    private User randomUser() {
        return User.newBuilder()
                .setUserId(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000, Long.MAX_VALUE)).build())
                .setUserUuid(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000, Long.MAX_VALUE)).build())
                .setHeadIcon(StringValue.newBuilder().setValue(RandomStringUtils.randomAlphanumeric(50)).build())
                .setNickname(StringValue.newBuilder().setValue(RandomStringUtils.randomAlphanumeric(20)).build())
                .setCreateTime(Int64Value.newBuilder().setValue(System.currentTimeMillis()).build())
                .setUpdateTime(Int64Value.newBuilder().setValue(System.currentTimeMillis()).build())
                .setGender(RandomUtils.nextBoolean() ? GenderEnum.MALE : GenderEnum.FEMALE)
                .setUserStatus(RandomUtils.nextBoolean() ? UserStatusEnum.ENABLED : UserStatusEnum.FORBIDDEN)
                .setMobile(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000000000L, 99999999999L)).build())
                .build();
    }

    @GetMapping("json")
    public int json() {
        User user = randomUser();
        String str = PbUtils.toJSONString(user);
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        write(user, data);
        return data.length;
    }
}
