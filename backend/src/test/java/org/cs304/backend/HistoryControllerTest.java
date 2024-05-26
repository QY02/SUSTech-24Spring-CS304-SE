package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.HistoryController;
import org.cs304.backend.entity.History;
import org.cs304.backend.mapper.HistoryMapper;
import org.cs304.backend.service.IHistoryService;
import org.cs304.backend.service.IUserInteractionService;
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
import static org.mockito.Mockito.when;

public class HistoryControllerTest {
    @Mock
    private IHistoryService historyService;
    @InjectMocks
    private HistoryController historyController;

    @Mock
    private IUserInteractionService userInteractionService;

    @Mock
    private HistoryMapper historyMapper;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return history event when getting history by user id")
    public void shouldReturnEventWhenGettingEventByUserId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", "12110141");
        request.setAttribute("loginUserType", 0);
        when(historyMapper.selectList(new QueryWrapper<History>().eq("user_id", "12110141"))).thenReturn(new ArrayList<>());
        Result result = historyController.getAllHistory(request, response, jsonObject);
//        System.out.println(result);
        assertEquals("200", result.getCode()); // 验证结果

    }

    @Test
    @DisplayName("Should return success when add new history")
    public void shouldReturnSuccessWhenAddingNewHistory() {
        com.alibaba.fastjson.JSONObject history = new com.alibaba.fastjson.JSONObject();
        // 设置事件的相关属性
        history.put("userId", "12110141");
        history.put("eventId", 80);
        Result result = historyController.addEventHistory(response, com.alibaba.fastjson2.JSONObject.from(history));

        assertEquals("200", result.getCode()); // 验证结果
    }
}
