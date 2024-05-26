package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.controller.SeatMapController;
import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.service.ISeatMapService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class SeatMapControllerTest {
    @InjectMocks
    SeatMapController seatMapController;

    @Mock
    ISeatMapService seatMapService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Test getSeatMapWithSeatsById")
    public void testGetSeatMapWithSeatsById() {
        request.setAttribute("loginUserType", constant_User.USER);
        JSONObject requestBody = new JSONObject();
        requestBody.put("seatMapId", 1);
        when(seatMapService.getSeatMapWithSeatsById(anyInt(), anyInt())).thenReturn(new SeatMap());
        Result result = seatMapController.getSeatMapWithSeatsById(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test getAllSeatMapTemplate")
    public void getAllSeatMapTemplate() {
        when(seatMapService.getAllSeatMapTemplate()).thenReturn(new JSONArray());
        Result result = seatMapController.getAllSeatMapTemplate(response);
        assertEquals("200", result.getCode());
    }
}
