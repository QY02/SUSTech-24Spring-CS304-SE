package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.constant.constant_CommentType;
import org.cs304.backend.controller.AdminController;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Event;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

    @InjectMocks
    AdminController adminController;

    @Mock
    IEventService eventService;

    @Mock
    EventMapper eventMapper;

    @Mock
    UserMapper userMapper;

    @Mock
    CommentMapper commentMapper;

    @Mock
    OrderRecordMapper orderRecordMapper;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return audit list when user is admin")
    public void shouldReturnAuditListWhenUserIsAdmin() {
        request.setAttribute("loginUserType", 0);
        when(eventService.getAuditList("0")).thenReturn(new JSONArray());

        Result result = adminController.getAuditList(request, response, "0");

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should throw ServiceException when user is not admin")
    public void shouldThrowServiceExceptionWhenUserIsNotAdmin() {
        request.setAttribute("loginUserType", 1);

        assertThrows(ServiceException.class, () -> adminController.getAuditList(request, response, "0"));

        request.setAttribute("loginUserType", 1);
        JSONObject requestBody = new JSONObject();
        requestBody.put("eventId", 1);
        requestBody.put("status", 1);
        requestBody.put("reason", "Test reason");

        assertThrows(ServiceException.class, () -> adminController.changeAudit(request, response, requestBody));
    }

    @Test
    @DisplayName("Should change audit status when user is admin")
    public void shouldChangeAuditStatusWhenUserIsAdmin() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("eventId", 1);
        requestBody.put("status", 1);
        requestBody.put("reason", "Test reason");

        Result result = adminController.changeAudit(request, response, requestBody);

        assertEquals("200", result.getCode());
    }

    @Test
@DisplayName("Should return homepage info when user is admin")
public void shouldReturnHomepageInfoWhenUserIsAdmin() {
    request.setAttribute("loginUserType", 0);
    when(eventMapper.selectCount(null)).thenReturn(10L);
    when(eventMapper.selectCount(new QueryWrapper<Event>().eq("status", constant_EventStatus.AUDITING))).thenReturn(2L);
    when(userMapper.selectCount(any())).thenReturn(5L);
        when(commentMapper.selectCount(new QueryWrapper<Comment>().eq("type", constant_CommentType.BLOG))).thenReturn(3L);
    when(orderRecordMapper.selectCount(null)).thenReturn(7L);

    Result result = adminController.getHomepage(request, response);

    assertEquals("200", result.getCode());
    JSONObject data = (JSONObject) result.getData();
    assertEquals(10, data.getInteger("event"));
    assertEquals(5, data.getInteger("user"));
    assertEquals(7, data.getInteger("order"));
}
}