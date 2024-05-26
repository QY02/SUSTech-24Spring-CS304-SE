package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.controller.LoginController;
import org.cs304.backend.controller.UserController;
import org.cs304.backend.entity.User;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IUserService;
import org.cs304.backend.utils.RedisUtil;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {
    MockHttpServletRequest request;

    MockHttpServletResponse response;
    @Mock
    UserMapper userMapper;
    @Mock
    private IUserService userService;

    @Mock
    private RedisUtil redisUtil;
    @InjectMocks
    private UserController userController;
    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Fail - Invalid Input (Blank ID or Password)")
    public void shouldReturnErrorForInvalidInput() {
        User user = new User();
        user.setId("");
        user.setPassword("");

//        MockHttpServletResponse response = new MockHttpServletResponse();

        Result result = loginController.login(response, user);

        assertEquals("400", result.getCode());
    }

    @Test
    @DisplayName("Fail - Exception on Invalid Credentials")
    public void shouldHandleException() {
        User user = new User();
        user.setId("testUser");
        user.setPassword("testPass");

        when(userService.login(any(User.class))).thenThrow(new RuntimeException("Invalid username or password"));

        Result result = loginController.login(response, user);

        assertEquals("400", result.getCode());
    }

    @Test
    @DisplayName("Fail - Invalid Input (Blank ID or Password)")
    public void shouldReturnErrorForInvalidInput_Register() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        JSONObject data = new JSONObject();
        JSONObject userJson = new JSONObject();
        userJson.put("id", "");
        userJson.put("password", "");
        data.put("user", userJson);

        Result result = loginController.register(response, data);

        assertEquals("400", result.getCode());
    }

    @Test
    @DisplayName("Success - Success on Registration")
    public void shouldHandleRegistrationException() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        JSONObject data = JSONObject.parseObject("{ \"user\": { \"id\": \"testUser\", \"password\": \"testPass\", \"name\": \"John\", \"email\": \"john@example.com\", \"phoneNumber\": \"1234567890\", \"department\": \"IT\", \"twoFactorAuthentication\": true } }");
        doNothing().when(userService).registerSearch(any(User.class));

//        when(userService.registerSearch(any(User.class))).thenThrow(new ServiceException("Invalid registration"));

        Result result = loginController.register(response, data);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Fail - Exception on Email Sending")
    public void shouldHandleEmailException() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        JSONObject data = JSONObject.parseObject("{ \"user\": { \"id\": \"testUser\", \"password\": \"testPass\", \"name\": \"John\", \"email\": \"john@example.com\", \"phoneNumber\": \"1234567890\", \"department\": \"IT\", \"twoFactorAuthentication\": true } }");

        doNothing().when(userService).registerSearch(any(User.class));
        doThrow(new ServiceException("Email service unavailable")).when(userService).sendEmail(anyString());

        Result result = loginController.register(response, data);

        assertEquals("500", result.getCode());
    }

    @Test
    @DisplayName("Success - Complete Registration")
    public void shouldRegisterSuccessfully() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        JSONObject data = JSONObject.parseObject("{ \"user\": { \"id\": \"22222222\", \"password\": \"testPass\", \"name\": \"John\", \"email\": \"john@example.com\", \"phoneNumber\": \"1234567890\", \"department\": \"IT\", \"twoFactorAuthentication\": true } }");

        doNothing().when(userService).registerSearch(any(User.class));
        doNothing().when(userService).sendEmail(anyString());
//        when(redisUtil.add(anyString(), anyString(), anyInt())).thenReturn(true);
        doNothing().when(redisUtil).add(anyString(), anyString(), anyInt());

        Result result = loginController.register(response, data);

        assertEquals("200", result.getCode()); // Assuming Result.SUCCESS is a constant representing success status
    }

    @Test
    public void testTwoFactorAuthentication_True() throws Exception {
        // Mock user data
        User user = new User();
        user.setId("123");
        user.setTwoFactorAuthentication(true);

        // Stubbing the userMapper selectById method
        when(userMapper.selectById(123)).thenReturn(user);

    }

    @Test
    public void testTwoFactorAuthentication_False() throws Exception {
        // Mock user data
        User user = new User();
        user.setId("123");
        user.setTwoFactorAuthentication(false);

        // Stubbing the userMapper selectById method
        when(userMapper.selectById(123)).thenReturn(user);

    }


}
