package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.OrderRecordController;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.service.IOrderRecordService;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class OrderRecordControllerTest {

    @InjectMocks
    OrderRecordController orderRecordController;

    @Mock
    OrderRecordMapper orderRecordMapper;

    @Mock
    EventMapper eventMapper;

    @Mock
    EventSessionMapper eventSessionMapper;

    @Mock
    SeatMapper seatMapper;

    @Mock
    IOrderRecordService orderRecordService;

    @Mock
    IEventService eventService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("add record")
    public void addOrderRecord() {
        OrderRecord orderRecord = new OrderRecord();
        request.setAttribute("loginUserType", 0);
        request.setAttribute("loginUserId", "10000000"); // 模拟本人
        orderRecord.setId(1);
        orderRecord.setEventId(1);
        orderRecord.setEventSessionId(1);

        Event event = new Event();
        event.setId(1);
        event.setStatus(1);
        event.setVisible(true);
        when(eventMapper.selectById(anyInt())).thenReturn(event);

        EventSession eventSession = new EventSession();
        eventSession.setVisible(true);
        eventSession.setEventId(1);
        eventSession.setRegistrationRequired(true);
        eventSession.setRegistrationStartTime(LocalDateTime.now().minusDays(1));
        eventSession.setRegistrationEndTime(LocalDateTime.now().plusDays(1));
        eventSession.setCurrentSize(10);
        eventSession.setMaxSize(20);
        when(eventSessionMapper.selectById(anyInt())).thenReturn(eventSession);

        Seat seat = new Seat();
        seat.setSeatMapId(1);
        seat.setSeatId("A1");
        seat.setAvailability(true);
        seat.setPrice(100);
        when(seatMapper.selectOne(any())).thenReturn(seat);
        when(orderRecordMapper.insert(any(OrderRecord.class))).thenReturn(1);


        // Mock eventService and orderRecordService behavior
        doNothing().when(eventService).submitBookingData(anyInt(), anyString(), any(OrderRecord.class));
        when(orderRecordService.getOne(any())).thenReturn(orderRecord);
        when(orderRecordService.updateById(any(OrderRecord.class))).thenReturn(true);

//        doNothing().when(eventService).submitBookingData(anyInt(), anyString(), any(OrderRecord.class));
//        when(orderRecordService.getOne(any())).thenReturn(orderRecord);
//        doNothing().when(orderRecordService).updateById(any(OrderRecord.class));

        Result result = orderRecordController.prePayInformation(response, request, orderRecord);
        assertEquals("200", result.getCode());
    }

}
