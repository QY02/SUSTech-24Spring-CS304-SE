package org.cs304.backend;

import static org.mockito.Mockito.*;

import org.cs304.backend.config.AliPayConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

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
    @Mock
    private AliPayConfig aliPayConfig;

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
    public void testAddOrderRecord() {
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

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    @Test
    @DisplayName("Test payNotify with TRADE_SUCCESS")
    public void testPayNotifyWithTradeSuccess() throws Exception {
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

        Result result = orderRecordController.prePayInformation(response, request, orderRecord);

        String randomSign = generateRandomString(341);
        // 设置请求参数
        request.setParameter("trade_status", "TRADE_SUCCESS");
        request.setParameter("out_trade_no", "1");
        request.setParameter("gmt_payment", "2024-05-24 12:00:00");
        request.setParameter("sign", randomSign);


        // Mock OrderRecord
        orderRecord.setId(1);
        when(orderRecordService.getOne(any())).thenReturn(orderRecord);
        doReturn(true).when(orderRecordService).updateById(any());

        orderRecordController.payNotify(request);

        // 验证 OrderRecordService 的 updateById 方法是否被调用
        verify(orderRecordService, times(1)).updateById(orderRecord);
    }

    @Test
    @DisplayName("Test payNotify with TRADE_CLOSED")
    public void testPayNotifyWithTradeClose() throws Exception {
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

        Result result = orderRecordController.prePayInformation(response, request, orderRecord);

        String randomSign = generateRandomString(341);
        // 设置请求参数
        request.setParameter("trade_status", "TRADE_CLOSED");
        request.setParameter("out_trade_no", "1");
        request.setParameter("gmt_payment", "2024-05-24 12:00:00");
        request.setParameter("sign", randomSign);


        // Mock OrderRecord
        orderRecord.setId(1);
        when(orderRecordService.getOne(any())).thenReturn(orderRecord);
        doReturn(true).when(orderRecordService).updateById(any());

        orderRecordController.payNotify(request);

        // 验证 OrderRecordService 的 updateById 方法是否被调用
        verify(orderRecordService, times(1)).updateById(orderRecord);
    }


    @Test
    @DisplayName("change pay result success")
    public void testPayResultSuccess() {
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

        JSONObject requestBody = new JSONObject();
        requestBody.put("time", "2024-05-24 12:00:00");
        requestBody.put("orderId", 1);
        requestBody.put("result", 1);
        result = orderRecordController.payResult(response, requestBody);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("change pay result")
    public void testPayResultFail() {
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

        JSONObject requestBody = new JSONObject();
        requestBody.put("time", "2024-05-24 12:00:00");
        requestBody.put("orderId", 1);
        requestBody.put("result", 0);
        result = orderRecordController.payResult(response, requestBody);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test getMyOrderRecord")
    public void testGetMyOrderRecord() {
        request.setAttribute("loginUserId", "12111111");
        JSONObject requestBody = new JSONObject();
        requestBody.put("eventId", 1);
        requestBody.put("mode", 0);
        when(orderRecordService.getMyOrderRecord(anyString(), anyInt(), anyInt())).thenReturn(new Object());
        Result result = orderRecordController.getMyOrderRecord(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

}
