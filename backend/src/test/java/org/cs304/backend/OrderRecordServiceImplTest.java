package org.cs304.backend;

import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.impl.OrderRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderRecordServiceImplTest {

    @InjectMocks
    private OrderRecordServiceImpl orderRecordService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EventSessionMapper eventSessionMapper;

    @Mock
    private OrderRecordMapper orderRecordMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(orderRecordService, "baseMapper", orderRecordMapper);
    }

    @Test
    @DisplayName("Test getMyOrderRecord")
    public void testGetMyOrderRecord() {
        String userId = "12111111";
        Integer eventId = 1;

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderRecordService.getMyOrderRecord(userId, eventId, null);
        });
        assertEquals("Invalid data", exception.getMessage());

        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setEventSessionId(1);
        orderRecord.setEventId(1);
        List<OrderRecord> orderRecordList = new ArrayList<>();
        orderRecordList.add(orderRecord);
        when(orderRecordMapper.selectList(any())).thenReturn(orderRecordList);
        when(eventSessionMapper.selectById(anyInt())).thenReturn(new EventSession());
        when(eventMapper.selectById(anyInt())).thenReturn(new Event());

        Object result = orderRecordService.getMyOrderRecord(userId, eventId, 0);
        assertNotNull(result);

        result = orderRecordService.getMyOrderRecord(userId, eventId, 1);
        assertNotNull(result);

        result = orderRecordService.getMyOrderRecord(userId, eventId, 2);
        assertNotNull(result);

        result = orderRecordService.getMyOrderRecord(userId, eventId, 3);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test getUnpaidOrderRecord")
    public void testGetUnpaidOrderRecord() {
        String userId = "12111111";
        Integer eventId = 1;

        Exception exception = assertThrows(ServiceException.class, () -> {
            orderRecordService.getUnpaidOrderRecord(userId, eventId, null);
        });
        assertEquals("Invalid data", exception.getMessage());

        List<OrderRecord> orderRecordList = new ArrayList<>();
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setId(1);
        orderRecord.setEventSessionId(1);
        orderRecord.setEventId(1);
        orderRecord.setSubmitTime(LocalDateTime.now().minusMinutes(11));
        orderRecordList.add(orderRecord);
        OrderRecord orderRecord1 = new OrderRecord();
        orderRecord1.setId(1);
        orderRecord1.setEventSessionId(1);
        orderRecord1.setEventId(1);
        orderRecord1.setSubmitTime(LocalDateTime.now().minusMinutes(9));
        orderRecordList.add(orderRecord1);
        when(orderRecordMapper.selectList(any())).thenReturn(orderRecordList);
        when(orderRecordMapper.updateById(any(OrderRecord.class))).thenReturn(1);
        when(eventSessionMapper.selectById(anyInt())).thenReturn(new EventSession());
        when(eventMapper.selectById(anyInt())).thenReturn(new Event());

        Object result = orderRecordService.getUnpaidOrderRecord(userId, eventId, 0);
        assertNotNull(result);

        result = orderRecordService.getUnpaidOrderRecord(userId, eventId, 1);
        assertNotNull(result);

        result = orderRecordService.getUnpaidOrderRecord(userId, eventId, 2);
        assertNotNull(result);

        result = orderRecordService.getUnpaidOrderRecord(userId, eventId, 3);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test getPaymentById")
    public void testGetPaymentById() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            orderRecordService.getPaymentById(null);
        });
        assertEquals("Invalid data", exception.getMessage());

        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setPaymentTime(null);
        when(orderRecordMapper.selectOne(any())).thenReturn(orderRecord);

        assertEquals(0, orderRecordService.getPaymentById(1));

        orderRecord.setPaymentTime(LocalDateTime.now());
        when(orderRecordMapper.selectOne(any())).thenReturn(orderRecord);

        assertEquals(1, orderRecordService.getPaymentById(1));
    }

    @Test
    @DisplayName("Test getEventOrderRecord with valid event id")
    public void getEventOrderRecord_ValidEventId() {
        Integer eventId = 1;
        when(orderRecordMapper.selectList(any())).thenReturn(new ArrayList<>());
        when(eventMapper.selectById(anyInt())).thenReturn(new Event());

        Object result = orderRecordService.getEventOrderRecord(eventId);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Test getEventOrderRecord with invalid event id")
    public void getEventOrderRecord_InvalidEventId() {
        Integer eventId = 999; // Non-existing event id
        when(orderRecordMapper.selectList(any())).thenReturn(new ArrayList<>());

        Object result = orderRecordService.getEventOrderRecord(eventId);
        assertNotNull(result);
    }

}