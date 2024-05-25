package org.cs304.backend;

import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.*;
import org.cs304.backend.service.impl.SeatMapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatMapServiceImplTest {

    @InjectMocks
    private SeatMapServiceImpl seatMapService;

    @Mock
    private SeatMapMapper seatMapMapper;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventSessionMapper eventSessionMapper;

    @Mock
    private SeatMapper seatMapper;

    @Mock
    private OrderRecordMapper orderRecordMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(seatMapService, "baseMapper", seatMapMapper);
    }

    @Test
    @DisplayName("Test getSeatMapWithSeatsById")
    public void testGetSeatMapWithSeatsById() {
        Integer seatMapId = 1;
        int userType = constant_User.USER;

        List<OrderRecord> orderRecordList = new ArrayList<>();
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setId(1);
        orderRecord.setEventSessionId(1);
        orderRecord.setSeatId("A1");
        orderRecordList.add(orderRecord);
        orderRecord = new OrderRecord();
        orderRecord.setId(2);
        orderRecord.setEventSessionId(2);
        orderRecord.setSeatId("A2");
        orderRecordList.add(orderRecord);
        when(orderRecordMapper.selectList(any())).thenReturn(orderRecordList);

        EventSession eventSession = new EventSession();
        eventSession.setSeatMapId(1);
        when(eventSessionMapper.selectById(anyInt())).thenReturn(eventSession);

        when(seatMapper.update(any())).thenReturn(1);

        when(orderRecordMapper.update(any())).thenReturn(1);

        when(seatMapMapper.selectById(any())).thenReturn(null);

        Exception exception = assertThrows(ServiceException.class, () -> {
            seatMapService.getSeatMapWithSeatsById(userType, seatMapId);
        });
        assertEquals("Seat map not exist", exception.getMessage());

        SeatMap seatMap = new SeatMap();
        seatMap.setId(seatMapId);
        seatMap.setType(1);
        seatMap.setData("""
                {
                  "size": {
                    "width": 80,
                    "height": 60
                  },
                  "seats": [{"id": "A1", "x": 0, "y": 0, "color": "#0059de"},
                    {"id": "A2", "x": 10, "y": 0, "color": "#0059de"},
                    {"id": "A3", "x": 20, "y": 0, "color": "#0059de"}
                   ]
                  }
                """);
        when(seatMapMapper.selectById(any())).thenReturn(seatMap);

        when(eventSessionMapper.selectList(any())).thenReturn(new ArrayList<>());

        exception = assertThrows(ServiceException.class, () -> {
            seatMapService.getSeatMapWithSeatsById(userType, seatMapId);
        });
        assertEquals("Seat map not exist", exception.getMessage());

        when(eventSessionMapper.selectList(any())).thenReturn(List.of(new EventSession()));

        when(eventMapper.exists(any())).thenReturn(false);

        exception = assertThrows(ServiceException.class, () -> {
            seatMapService.getSeatMapWithSeatsById(userType, seatMapId);
        });
        assertEquals("Seat map not exist", exception.getMessage());

        when(eventMapper.exists(any())).thenReturn(true);

        Seat seat0 = new Seat(1, "A1", "普通", true, 100);
        Seat seat1 = new Seat(1, "A2", "普通", false, 100);
        Seat seat2 = new Seat(1, "A3", "普通", true, 200);
        List<Seat> seatList = new ArrayList<>();
        seatList.add(seat0);
        seatList.add(seat1);
        seatList.add(seat2);
        when(seatMapper.selectList(any())).thenReturn(seatList);

        SeatMap result = seatMapService.getSeatMapWithSeatsById(userType, seatMapId);
        assertEquals(seatMapId, result.getId());
    }
}