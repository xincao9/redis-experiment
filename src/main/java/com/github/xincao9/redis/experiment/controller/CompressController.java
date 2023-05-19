package com.github.xincao9.redis.experiment.controller;

import com.github.xincao9.redis.experiment.GenderEnum;
import com.github.xincao9.redis.experiment.User;
import com.github.xincao9.redis.experiment.UserStatusEnum;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("compress")
public class CompressController {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @GetMapping("protobuf")
    public void protobuf() {
        User user = randomUser();
        redisTemplate.opsForValue().set(String.format("%d", user.getMobile().getValue()), user.toByteArray());
    }

    private User randomUser() {
        return User.newBuilder()
                .setUserId(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000, Long.MAX_VALUE)).build())
                .setUserUuid(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000, Long.MAX_VALUE)).build())
                .setHeadIcon(StringValue.newBuilder().setValue(RandomStringUtils.random(50)).build())
                .setNickname(StringValue.newBuilder().setValue(RandomStringUtils.random(20)).build())
                .setCreateTime(Int64Value.newBuilder().setValue(System.currentTimeMillis()).build())
                .setUpdateTime(Int64Value.newBuilder().setValue(System.currentTimeMillis()).build())
                .setGender(RandomUtils.nextBoolean() ? GenderEnum.MALE : GenderEnum.FEMALE)
                .setUserStatus(RandomUtils.nextBoolean() ? UserStatusEnum.ENABLED : UserStatusEnum.FORBIDDEN)
                .setMobile(Int64Value.newBuilder().setValue(RandomUtils.nextLong(10000000000L, 99999999999L)).build())
                .build();
    }

    @GetMapping("json")
    public void json() {
        User user = randomUser();
        redisTemplate.opsForValue().set(String.format("%d", user.getMobile().getValue()), user.toByteArray());
    }
}
