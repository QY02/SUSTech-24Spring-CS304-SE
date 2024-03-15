package org.cs304.backend.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import org.cs304.backend.utils.Encryption;
import org.cs304.backend.utils.Result;
import org.cs304.backend.entity.*;
import org.cs304.backend.service.*;
import org.cs304.backend.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.cs304.backend.constant.constant_User;

import java.util.List;
import java.util.Objects;

/**
 * 用户信息表 前端控制器
 *
 * @since 2023-10-20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUserService userService;

    @Resource
    private RedisUtil redisUtil;


    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result add(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            if (user.getDepartment()==null){
                user.setDepartment("");
            }
            if (user.getPassword()==null||user.getEmail()==null||user.getType()==null||user.getName()==null){
                throw new RuntimeException("User information incomplete");
            }
            userService.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof DuplicateKeyException) {
                return Result.error(response, "400", "User already exists");
            } else {
                return Result.error(response, "400", "System error");
            }
        }
        log.info("User added: " + user.getId());
        return Result.success(response);
    }

    /**
     * 批量新增用户
     */
    @PostMapping("/add/batch")
    public Result addBatch(HttpServletRequest request, HttpServletResponse response, @RequestBody List<User> users) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            saveUser(users);
            userService.saveBatch(users);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof DuplicateKeyException) {
                if (e.getMessage().contains("User already exists")){
                    return Result.error(response, "400", e.getMessage());
                }
                return Result.error(response, "400", "User already exists");
            }  else {
                return Result.error(response, "400", "System error");
            }
        }
        for (User user : users) {
            log.info("新增用户: " + user.getId());
        }
        return Result.success(response);
    }

    private void saveUser(@RequestBody List<User> users) {
        users.forEach(user -> {
            if (user.getDepartment()==null){
                user.setDepartment("");
            }
            if (user.getPassword()==null||user.getEmail()==null||user.getType()==null||user.getName()==null){
                throw new RuntimeException("User information incomplete");
            }
            if (userService.getOne(new QueryWrapper<User>().eq("id", user.getId())) != null) {
                throw new DuplicateKeyException("User already exists: " + user.getId());
            }else {
                userService.save(user);
            }
        });
    }

    /**
     * 批量导入用户
     *
     * @param file 传入的excel文件对象
     * @return 导入结果
     */
    @PostMapping("/import")
    public Result importUser(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            List<User> userList = reader.readAll(User.class);//写入数据到数据库
            saveUser(userList);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof DuplicateKeyException) {
                if (e.getMessage().contains("User already exists")){
                    return Result.error(response, "400", e.getMessage());
                }
                return Result.error(response, "400", "User already exists");
            }  else {
                return Result.error(response, "400", e.getMessage());
            }
        }
        return Result.success(response);
    }

    /**
     * 修改用户信息（本方法不可修改密码、邮箱、学号）
     */
    @PutMapping("/update")
    public Result update(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
        try {
            if (!request.getAttribute("loginUserId").equals(user.getId())) {
                log.error("You can only change your own information");
                return Result.error(response, "403", "You can only change your own information");
            }
            if (user.getPassword()!=null){
                log.error("Unable to change password");
                return Result.error(response, "400", "Unable to change password");
            }
            if (user.getEmail()!=null){
                log.error("Unable to change email");
                return Result.error(response, "400", "Unable to change email");
            }
            userService.updateById(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Alter user failed");
        }
        return Result.success(response);
    }

    /**
     * 修改用户信息（管理员）
     */
    @PutMapping("/update/admin")
    public Result updateAdmin(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            userService.updateById(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Alter user failed");
        }
        return Result.success(response);
    }


    /**
     * 修改密码
     */
    @PutMapping("/update/pass")
    public Result updatePass(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject user) {
        try {
            if (!request.getAttribute("loginUserId").equals(user.get("id"))) {
                log.error("You can only change your own information");
                return Result.error(response, "403", "You can only change your own information");
            }
            if (userService.getOne(new QueryWrapper<User>().eq("id", user.getString("id")).eq("password", Encryption.encrypt(user.getString("old_password"))))==null) {
                log.error("Password error");
                return Result.error(response, "401", "Password error");
            }
            User user1 = new User();
            user1.setId((String) user.get("id"));
            user1.setPassword(Encryption.encrypt(user.getString("new_password")));
            userService.updateById(user1);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Alter user failed");
        }
        return Result.success(response);
    }

    /**
     * 修改邮箱
     */
    @PutMapping("/update/email")
    public Result updateEmail(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
        try {
            if (!request.getAttribute("loginUserId").equals(user.getId())) {
                log.error("You can only change your own information");
                return Result.error(response, "403", "You can only change your own information");
            }
            if (userService.getOne(new QueryWrapper<User>().eq("id", user.getId()).eq("password",Encryption.encrypt(user.getPassword())))==null) {
                log.error("Password error");
                return Result.error(response, "401", "Password error");
            }
            if (user.getEmail()==null){
                log.error("Email cannot be empty");
                return Result.error(response, "401", "Email cannot be empty");
            }
            if (userService.getOne(new QueryWrapper<User>().eq("email",user.getEmail()))!=null) {
                log.error("Email already exists");
                return Result.error(response, "401", "Email already exists");
            }
            userService.sendEmail(user.getEmail());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Alter user failed");
        }
        return Result.success(response);
    }

    /**
     * 忘记密码
     */
    @PutMapping("/forgetPass")
    public Result forgetPass(HttpServletResponse response, @RequestBody User user) {
        try {
            if (user.getEmail()==null){
                log.error("Email cannot be empty");
                return Result.error(response, "401", "Email cannot be empty");
            }
            if (userService.getOne(new QueryWrapper<User>().eq("email",user.getEmail()))==null) {
                log.error("User not exist");
                return Result.error(response, "401", "User not exist");
            }
            userService.sendEmail(user.getEmail());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Alter user failed");
        }
        return Result.success(response);
    }

    /**
     * 忘记密码-验证邮箱
     * {
     * "password":"123456",
     * "email":"1223@qq.com",
     * "code": "123456"
     * }
     *
     * @param emailVerify 邮箱和验证码
     */
    @PutMapping("/forgetPass/emailVerify")
    public Result forgetPassEmailVerify(@NotNull HttpServletResponse response, @RequestBody JSONObject emailVerify) {
        try {
            if (StrUtil.isBlank(emailVerify.getString("email")) || StrUtil.isBlank(emailVerify.getString("code"))) {
                log.error("Validation error");
                return Result.error(response, "401", "Validation error");
            }
            if (!Objects.equals(redisUtil.get(emailVerify.getString("code"), false, true), emailVerify.getString("email"))) {
                log.error("Validation error, please try again");
                return Result.error(response, "401","Validation error, please try again");
            }
            User user = userService.getOne(new QueryWrapper<User>().eq("email", emailVerify.getString("email")));
            user.setPassword(Encryption.encrypt(emailVerify.getString("password")));
            userService.updateById(user);
            return Result.success(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "500", e.getMessage());
        }
    }

    /**
     * 修改邮箱-验证注册邮箱
     * {
     * "id":"12112003",
     * "email":"1223@qq.com",
     * "code": "123456"
     * }
     *
     * @param emailVerify 邮箱和验证码
     * @return user 用户信息
     */
    @PutMapping("/update/emailVerify")
    public Result updateEmailVerify(@NotNull HttpServletResponse response,HttpServletRequest request, @RequestBody JSONObject emailVerify) {
        try {
            if (StrUtil.isBlank(emailVerify.getString("email")) || StrUtil.isBlank(emailVerify.getString("code"))) {
                log.error("Validation error");
                return Result.error(response, "401", "Validation error");
            }
            if (!request.getAttribute("loginUserId").equals(emailVerify.getString("id"))) {
                log.error("You can only change your own information");
                return Result.error(response, "403", "You can only change your own information");
            }
            if (!Objects.equals(redisUtil.get(emailVerify.getString("code"), false, true), emailVerify.getString("email"))) {
                log.error("Validation error, please try again");
                return Result.error(response, "401","Validation error, please try again");
            }
            User user = new User();
            user.setId(emailVerify.getString("id"));
            user.setEmail(emailVerify.getString("email"));
            userService.updateById(user);
            return Result.success(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "500", e.getMessage());
        }
    }


    /**
     * 获取当前用户ID
     */
    @Operation(summary = "获取当前用户ID",description = "这是一个详细的描述，你可以在这里添加更多的信息。")
    @PostMapping("/getMyID")
    public Result getMyID(HttpServletResponse response, HttpServletRequest request) {
        String id = (String) request.getAttribute("loginUserId");
        return Result.success(response, id);
    }


    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    public Result delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            userService.deleteUser(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Delete user failed");
        }
        return Result.success(response);
    }

    /**
     * 自行销户
     */
    @PostMapping("/delete/self")
    public Result deleteSelf(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) {
        if (!request.getAttribute("loginUserId").equals(user.getId())) {
            log.error("You can only delete your own account");
            return Result.error(response, "403", "You can only delete your own account");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("id", user.getId()).eq("password",Encryption.encrypt(user.getPassword())))==null) {
            log.error("Password error");
            return Result.error(response, "401", "Password error");
        }
        try {
            userService.deleteUser(user.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Delete user failed");
        }
        return Result.success(response);
    }


    /**
     * 批量删除用户
     */
    @PostMapping("/delete/batch")
    public Result deleteBatch(HttpServletRequest request, HttpServletResponse response, @RequestBody List<String> ids) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            if (ids.isEmpty()){
                return Result.success(response);
            }
            userService.deleteUsers(ids);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "400", "Delete user failed");
        }
        return Result.success(response);
    }

    /**
     * 查询用户信息
     */
    @PostMapping("/get/{id}")
    public Result get(HttpServletResponse response, @PathVariable String id) {
        try {
            User user = userService.getById(id);
            user.setPassword(null);
            return Result.success(response, user);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(response, "400", "Get user failed");
        }
    }

    /**
     * 批量查询用户信息
     */
    @PostMapping("/get/batch")
    public Result list(HttpServletResponse response, @RequestBody List<String> ids) {
        try {
            if (ids.isEmpty()){
                return Result.success(response);
            }
            List<User> userList = userService.listByIds(ids);
            userList.forEach(user -> user.setPassword(null));
            return Result.success(response, userList);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(response, "400", "Get user failed");
        }
    }

    /**
     * 批量查询所有用户信息
     */
    @PostMapping("/getAll")
    public Result listAll(HttpServletResponse response,HttpServletRequest request) {
        try {
            int userType = (int) request.getAttribute("loginUserType");
            if (userType != constant_User.ADMIN.getValue()) {
                log.error("Only admin can alter");
                return Result.error(response, "403", "Only admin can alter");
            }
            List<User> userList = userService.list();
            userList.forEach(user -> user.setPassword(null));
            return Result.success(response, userList);
        }catch (Exception e){
            log.error(e.getMessage());
            return Result.error(response, "400", "Get user failed");
        }
    }
}