package org.cs304.backend.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private final long timeout = 3600;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    public void add(String value, String key) {
        if ((value != null) && (key != null)) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            stringRedisTemplate.opsForValue().set(value, key, timeout, timeUnit);
        }
    }

    public void add(String key, String value, long timeout) {
        if ((key != null) && (value != null)) {
            stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
    }

    public void add(String key, long timeout) {
        if (key != null) {
            stringRedisTemplate.opsForValue().set(key, "", timeout, timeUnit);
        }
    }

    public String get(String userToken, boolean resetExpireTime, boolean delete) {
        if (userToken == null) {
            return null;
        } else {
            if (resetExpireTime) {
                stringRedisTemplate.expire(userToken, timeout, timeUnit);
            }
            String value = stringRedisTemplate.opsForValue().get(userToken);
            if (value == null) {
                return null;
            }
            if (delete) {
                stringRedisTemplate.delete(userToken);
            }
            return value;
        }
    }

    public String getInterceptor(String userToken, boolean resetExpireTime, boolean delete) {
        if (userToken == null) {
            return null;
        } else {
            String value;
            if (resetExpireTime) {
                stringRedisTemplate.expire(userToken, timeout, timeUnit);
                value = stringRedisTemplate.opsForValue().get(userToken);
                if (value != null) {
                    stringRedisTemplate.opsForValue().set(value, userToken, timeout, timeUnit);
                }
            }
            else {
                value = stringRedisTemplate.opsForValue().get(userToken);
            }
            if (value == null) {
                return null;
            }
            if (delete) {
                stringRedisTemplate.delete(userToken);
            }
            return value;
        }
    }

//    public User generateToken(User user) {
//        String token = (UUID.randomUUID().toString() + UUID.randomUUID()).replaceAll("-", "");
//        String dbToken = stringRedisTemplate.opsForValue().get(user.getId());
//        if (dbToken != null) {
//            stringRedisTemplate.delete(user.getId());
//            stringRedisTemplate.delete(dbToken);
//        }
//        while (true) {
//            if (get(token, false, false) == null) {
//                break;
//            } else {
//                token = (UUID.randomUUID().toString() + UUID.randomUUID()).replaceAll("-", "");
//            }
//        }
//        add(user.getId(), token);
//        user.setPassword(token);
//        return user;
//    }
}
