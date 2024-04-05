package org.cs304.backend.controller;

import com.alibaba.fastjson2.*;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.User;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.service.IUserService;
import org.cs304.backend.utils.Encryption;
import org.cs304.backend.utils.RedisUtil;
import org.cs304.backend.utils.Result;
import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import cn.hutool.core.util.StrUtil;



import java.util.Objects;


/**
 * @author: TaoYiCheng
 * @author: QY
 * @createDate: 2024.3.9
 * @description: 用户登录注册控制器
 */
@Slf4j
@RestController
public class LoginController {
    @Resource
    private IUserService userService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 登录
     *
     * @param user 用户信息
     * @return 成功
     */
    @PostMapping("/login")
    public Result login(@NotNull HttpServletResponse response, @RequestBody User user) {
        try {
            if (StrUtil.isBlank(user.getId()) || StrUtil.isBlank(user.getPassword())) {
                log.error("Invalid Input");
                return Result.error(response, "400", "Invalid Input");
            }
            System.out.println(user.getId()+"  "+user.getPassword());
            user = userService.login(user);
//            user.setPassword(null);
//            System.out.println(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "401", "Invalid username or password");
        }
        if (user.getTwoFactorAuthentication()){
            return Result.success(response);
        }else return Result.success(response, user);
    }

    /**
     * 二次验证
     * @param user 用户信息(包含id)
     * @return 是否开启二次验证
     */
    @PostMapping("/twoFactorAuthentication")
    public Result twoFactorAuthentication(@NotNull HttpServletResponse response,@RequestBody User user) {
        user = userService.getById(user.getId());
        if (user.getTwoFactorAuthentication()){
            return Result.success(response,true);
        }else return Result.success(response,false);
    }

    /**
     * 注册
     *
     * @param user 用户信息
     * @return 成功
     */
    @PostMapping("/register")
    public Result register(@NotNull HttpServletResponse response, @RequestBody User user) {
        if (StrUtil.isBlank(user.getId()) || StrUtil.isBlank(user.getPassword())) {
            log.error("Invalid Input");
            return Result.error(response, "400", "Invalid Input");
        }
        try {
            userService.registerSearch(user);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return Result.error(response, "400", e.getMessage());
        }
        try {
            userService.sendEmail(user.getEmail());
            user.setTwoFactorAuthentication(false);//暂且设置成false
            redisUtil.add(user.getEmail(), JSON.toJSONString(JSON.toJSON(user)), 300);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return Result.error(response, "500", e.getMessage());
        }
        return Result.success(response);
    }

    /**
     * 验证注册邮箱
     * {
     * "email":"1223@qq.com",
     * "code": "123456"
     * }
     *
     * @param emailVerify 邮箱和验证码
     * @return user 用户信息
     */
    @PostMapping("/registerEmailVerify")
    public Result registerEmailVerify(@NotNull HttpServletResponse response, @RequestBody JSONObject emailVerify) {
        try {
            if (StrUtil.isBlank(emailVerify.getString("email")) || StrUtil.isBlank(emailVerify.getString("code"))) {
                log.error("Invalid Input");
                return Result.error(response, "401", "Verification error, please try again");
            }
            if (!Objects.equals(redisUtil.get(emailVerify.getString("code"), false, true), emailVerify.getString("email"))) {
                log.error("Verification error, please try again");
                return Result.error(response, "401", "Verification error, please try again");
            }
            User user;
            user = JSON.parseObject(redisUtil.get(emailVerify.getString("email"), false, true), User.class);
            user.setType(constant_User.USER);
            user.setPassword(Encryption.encrypt(user.getPassword()));
            userService.save(user);
            user.setPassword(null);
            return Result.success(response, user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "500", e.getMessage());
        }
    }


    /**
     * 通过邮箱登录
     * {
     * "email":"1223@qq.com",
     * "code": "123456"
     * }
     *
     * @param emailVerify 邮箱和验证码
     * @return user 用户信息
     */
    @PostMapping("/loginWithEmail")
    public Result loginWithEmail(@NotNull HttpServletResponse response, @RequestBody JSONObject emailVerify) {
        if (StrUtil.isBlank(emailVerify.getString("email")) || StrUtil.isBlank(emailVerify.getString("code"))) {
            log.error("Invalid Input");
            return Result.error(response, "401", "Verification error, please try again");
        }
        User user = userService.loginWithEmail(emailVerify);
        user.setPassword(null);
        return Result.success(response, user);
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 成功或失败
     */
    @PostMapping("/sendEmail/{email}")
    public Result sendEmail(HttpServletResponse response, @PathVariable("email") String email) {
        if (StrUtil.isBlank(email)) {
            log.error("Invalid Input");
            return Result.error(response, "400", "Invalid Input");
        }
        try {
            userService.verifyAndSendEmail(email);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            return Result.error(response, "500", e.getMessage());
        }
        return Result.success(response);
    }

}