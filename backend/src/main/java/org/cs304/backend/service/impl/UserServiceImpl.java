package org.cs304.backend.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.User;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IUserService;
import org.cs304.backend.utils.Encryption;
import org.cs304.backend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: TaoYiCheng
 * @author: QY
 * @createDate: 2023/10/17
 * @description: 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderAddress;

    /**
     * 登录
     *
     * @param user 用户信息
     * @return user 用户信息
     */
    @Override
    public User login(User user) {
        User dbuser = baseMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()).eq("password", Encryption.encrypt(user.getPassword())));
        if (dbuser == null) {
            throw new ServiceException("Invalid username or password");
        }
        return redisUtil.generateToken(dbuser);
    }


    /**
     * 查询是否重复注册
     *
     * @param user 用户信息
     */
    @Override
    public void registerSearch(User user) {
        User dbuser = baseMapper.selectById(user.getId());
        if (dbuser != null) {
            throw new ServiceException("User ID already exists");
        }
        dbuser = baseMapper.selectOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        if (dbuser != null) {
            throw new ServiceException("User Email already exists");
        }
    }



    /**
     * 通过邮箱登录
     *
     * @param jsonObject 邮箱和验证码
     * @return user 用户信息
     */
    @Override
    public User loginWithEmail(JSONObject jsonObject) {
        if (!Objects.equals(redisUtil.get(jsonObject.getString("code"), false, true), jsonObject.getString("email"))) {
            throw new ServiceException("Verification error, please try again");
        }
        User dbuser = baseMapper.selectOne(new QueryWrapper<User>().eq("email", jsonObject.getString("email")));
        return redisUtil.generateToken(dbuser);
    }

    /**
     * 发送邮箱验证码操作
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        message.setSentDate(new Date());
        message.setSubject("[C.E.E.C.] Email Verification");
        String code = RandomUtil.randomNumbers(6);
        message.setText("【Campus Events and Entertainment Center】Verification code: " + code + " (valid for 5 minutes).\nYou are using this email to log in to Campus Events and Entertainment Center.\nIf you are not operating, please ignore this email.");
        message.setTo(email);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new ServiceException("Email sending failed");
        }
        redisUtil.add(code, email, 300);
    }

    /**
     * 验证邮箱并发送验证码
     *
     * @param email 邮箱
     */
    @Override
    public void verifyAndSendEmail(String email) {
        User dbuser = baseMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (dbuser == null) {
            throw new ServiceException("User not found");
        }
        try {
            sendEmail(email);
        } catch (ServiceException e) {
            throw new ServiceException("Email sending failed");
        }

    }

    @Override
    public void deleteUser(String id) {
      baseMapper.deleteById(id);
    }

    @Override
    public void deleteUsers(List<String> ids) {
        ids.forEach(this::deleteUser);
    }
}