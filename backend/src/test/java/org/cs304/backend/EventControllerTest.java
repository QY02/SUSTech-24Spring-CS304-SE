package org.cs304.backend; /**
 * @author zyp
 * @date 2024/5/23 23:14
 * @description
 **/
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.controller.EventController;
import org.cs304.backend.controller.AdminController;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Event;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventControllerTest {

    @Mock
    private IEventService eventService;

    @InjectMocks
    private EventController eventController;
    @InjectMocks
    private AdminController adminController;

    @Mock
    EventMapper eventMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return success when posting new event")
    public void shouldReturnSuccessWhenPostingNewEvent() {
        JSONObject event = new JSONObject();
        // 设置事件的相关属性

        when(eventService.createEventStart(com.alibaba.fastjson2.JSONObject.from(event))).thenReturn(com.alibaba.fastjson2.JSONObject.from(new JSONObject())); // 设置mock返回值

        Result result = eventController.postNewEvent(response, com.alibaba.fastjson2.JSONObject.from(event));

        assertEquals("200", result.getCode()); // 验证结果
    }

    @Test
    @DisplayName("success - get not exist event detail")
    public void shouldReturnEventDetail() {
        Event event = new Event();
        event.setName("Test Event");
        event.setStatus(1);
        event.setId(1);

        when(eventMapper.insert(event)).thenReturn(1);

        when(eventService.createEventStart(com.alibaba.fastjson2.JSONObject.from(event))).thenReturn(com.alibaba.fastjson2.JSONObject.from(new JSONObject())); // 设置mock返回值
//        Result result = eventController.postNewEvent(response, (com.alibaba.fastjson2.JSONObject) JSON.toJSON(event));
//        assertEquals("200", result.getCode()); // 验证结果
        Result result = eventController.getEventDetail(request, response, 1);
        // not exist
        assertEquals("404", result.getCode()); // 验证结果
    }

}

