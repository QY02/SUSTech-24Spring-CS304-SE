package org.cs304.backend;

/**
 * @author zyp
 * @date 2024/5/23 23:04
 * @description
 **/
import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.controller.NotificationController;
import org.cs304.backend.service.INotificationService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationControllerTest {

    @InjectMocks
    NotificationController notificationController;

    @Mock
    INotificationService notificationService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setAttribute("loginUserId", "testUserId"); // 设置登录用户ID
    }

    @Test
    @DisplayName("Should return success when posting event pass notification")
    public void shouldReturnSuccessWhenPostingEventPassNotification() {
        int eventId = 123; // 设置事件ID

        doNothing().when(notificationService).insertEventPassNotification("testUserId", eventId);

        Result result = notificationController.postEventPass(response, request, eventId);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when posting event not pass notification")
    public void shouldReturnSuccessWhenPostingEventNotPassNotification() {
        int eventId = 123; // 设置事件ID
        JSONObject data = new JSONObject();
        data.put("comment", "Test comment"); // 设置反馈评论

        doNothing().when(notificationService).insertEventNotPassNotification("testUserId", eventId, "Test comment");

        Result result = notificationController.postEventNotPass(response, request, eventId, data);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when posting reserve session notification")
    public void shouldReturnSuccessWhenPostingReserveSessionNotification() {
        int eventSessionId = 123; // 设置事件场次ID

        doNothing().when(notificationService).insertReserveSessionNotification("testUserId", eventSessionId);

        Result result = notificationController.postReserveSession(response, request, eventSessionId);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when posting admin notification")
    public void shouldReturnSuccessWhenPostingAdminNotification() {
        JSONObject data = new JSONObject();
        data.put("title", "Test title");
        data.put("content", "Test content");

        // 设置HttpServletRequest对象的属性值
        request.setAttribute("loginUserId", "testUserId");
        request.setAttribute("loginUserType", constant_User.ADMIN);

        // 在测试中直接调用方法
        notificationController.postAdminNotification(response, request, "testNotifiedUserId", data);

        // 验证结果是否为200
        assertEquals(200, response.getStatus());
    }
}

