package org.cs304.backend;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.UserController;
import org.cs304.backend.entity.User;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IUserFavoriteTypeService;
import org.cs304.backend.service.IUserService;
import org.cs304.backend.utils.RedisUtil;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.cs304.backend.constant.constant_User;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserMapper userMapper;

    @Mock
    IUserService userService;

    @Mock
    IUserFavoriteTypeService userFavoriteTypeService;

    @Mock
    private MockMvc mockMvc;

    @Mock
    private RedisUtil redisUtil;


    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//        request.setAttribute("loginUserType", 0); // 模拟管理员
    }

    @Test
    @DisplayName("Should return jsonObject when getting userId")
    public void shouldReturnUserFavoriteTypeWhenGettingById() {
        //TODO: 测试getUserFavoriteType
    }

    @Test
    @DisplayName("Should return jsonObject when change favoriteType")
    public void shouldReturnUserFavoriteTypeWhenChangeFavoriteType() {
        //TODO: 测试changeUserFavoriteType
    }

    @Test
    @DisplayName("success - adding a user")
    public void shouldReturnSuccessWhenAddingUser() {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");

        request.setAttribute("loginUserType", 0); // 模拟管理员


        when(userMapper.insert(user)).thenReturn(1);

        Result result = userController.add(response, request, user);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("success - non-admin add a new user")
    public void shouldReturnErrorWhenNonAdminTriesToAddNewUser() {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");

        request.setAttribute("loginUserType", 1); // 非管理员

        Result result = userController.add(response, request, user);

        assertEquals("403", result.getCode());
    }

    @Test
    @DisplayName("success - error when user information is incomplete")
    public void shouldReturnErrorWhenUserInformationIsIncomplete() {
        User user = new User();
        user.setId("10000000");
        // 缺少必要的字段
        user.setEmail("test@example.com");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        Result result = userController.add(response, request, user);
        assertEquals("400", result.getCode());
    }

    @Test
    @DisplayName("success - updating own information without restricted fields")
    public void shouldReturnSuccessWhenUpdatingInformation() {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        when(userMapper.insert(user)).thenReturn(1);
        Result result = userController.add(response, request, user);

        user.setName("Test User Change");
        user.setPassword(null);
        user.setEmail(null);
        request.setAttribute("loginUserId", "10000000");
        when(userMapper.updateById(user)).thenReturn(1);
        result = userController.update(response, request, user);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when updating an admin user")
    public void shouldReturnSuccessWhenUpdateUserAdmin() {
        //TODO: 检查user/update/admin
    }

    @Test
    @DisplayName("Should return success when updating the password")
    public void shouldReturnSuccessWhenUpdatePassword() {
        //TODO: 检查user/update/pass
    }

    @Test
    @DisplayName("success - success when updating the email")
    public void shouldReturnSuccessWhenUpdateEmail() {
        User user = new User();
        user.setId("10000000");
        user.setName("testUser");
        user.setEmail("test@mail.com");
        user.setDepartment("计算机系");
        user.setPassword("11111111");
        when(userMapper.insert(user)).thenReturn(1);
        user.setEmail("testChange@mail.com");
        request.setAttribute("loginUserId", "10000000");
        Result result = userController.updateEmail(response, request, user);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return a user when getting a user by id")
    public void shouldReturnUserWhenGettingUserById() {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        when(userMapper.insert(user)).thenReturn(1);
        Result result = userController.add(response, request, user);

        request.setAttribute("loginUserId", "10000000");

        when(userMapper.selectById("10000000")).thenReturn(new User());

        result = userController.get(response, "10000000");
        // 因为没password
        assertEquals("400", result.getCode());
    }


    @Test
    @DisplayName("success - update Email Verify")
    public void testUpdateEmailVerifySuccess() throws Exception {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        when(userMapper.insert(user)).thenReturn(1);
        Result result = userController.add(response, request, user);

        // 模拟 Redis 返回
        Mockito.when(redisUtil.get(Mockito.eq("valid-code"), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn("test@example.com");

        // 创建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", "10000000");
        requestBody.put("email", "test@example.com");
        requestBody.put("code", "valid-code");
        request.setAttribute("loginUserId", "10000000"); // 模拟本人
        result = userController.updateEmailVerify(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("success - update Email Verify with blank data")
    public void testUpdateEmailVerifyBlank() throws Exception {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        when(userMapper.insert(user)).thenReturn(1);
        Result result = userController.add(response, request, user);

        // 模拟 Redis 返回
        Mockito.when(redisUtil.get(Mockito.eq("valid-code"), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn("test@example.com");

        // 创建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", "10000000");
//        requestBody.put("email", "test@example.com");
        requestBody.put("code", "valid-code");
        request.setAttribute("loginUserId", "10000000"); // 模拟本人
        result = userController.updateEmailVerify(response, request, requestBody);
        assertEquals("401", result.getCode());
    }

    @Test
    @DisplayName("success - update Email Verify with invalid data")
    public void testUpdateEmailVerifyInvalid() throws Exception {
        User user = new User();
        user.setId("10000000");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setType(1);
        user.setName("Test User");
        request.setAttribute("loginUserType", 0); // 模拟管理员
        when(userMapper.insert(user)).thenReturn(1);
        Result result = userController.add(response, request, user);

        // 模拟 Redis 返回
        Mockito.when(redisUtil.get(Mockito.eq("valid-code"), Mockito.eq(false), Mockito.eq(true)))
                .thenReturn("test@example.com");

        // 创建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", "10000000");
        requestBody.put("email", "test@example.com");
        requestBody.put("code", "invalid-code");
        request.setAttribute("loginUserId", "10000000"); // 模拟本人
        result = userController.updateEmailVerify(response, request, requestBody);
        assertEquals("401", result.getCode());
    }

}
