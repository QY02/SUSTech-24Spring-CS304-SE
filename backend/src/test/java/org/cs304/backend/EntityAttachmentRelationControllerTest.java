package org.cs304.backend;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.controller.AttachmentController;
import org.cs304.backend.controller.EntityAttachmentRelationController;
import org.cs304.backend.controller.EventController;
import org.cs304.backend.controller.AdminController;
import org.cs304.backend.entity.Attachment;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Event;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.service.IEntityAttachmentRelationService;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class EntityAttachmentRelationControllerTest {
    @Mock
    private IEventService eventService;
    @Mock
    private IEntityAttachmentRelationService entityAttachmentRelationService;

    @InjectMocks
    private EntityAttachmentRelationController entityAttachmentRelationController;

    @Mock
    EventMapper eventMapper;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testGetAttachmentByEntityId() {
        // 构造请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("entity_type", 1);
        requestBody.put("entity_id", 1);
        requestBody.put("attachment_type", 0);
        request.setAttribute("loginUserType", 0);

        // 设置entityAttachmentRelationService的返回值
        when(entityAttachmentRelationService.getAttachment(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(new Attachment());

        // 调用方法
        Result result = entityAttachmentRelationController.getAttachmentByEntityId(response, request, com.alibaba.fastjson2.JSONObject.from(requestBody));

        // 验证返回结果
        assertEquals("200", result.getCode());
        // 可以根据具体情况进一步验证返回的数据
    }

}
