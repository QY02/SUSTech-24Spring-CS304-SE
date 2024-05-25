package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.SeatMapMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.impl.EventSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * @author zyp
 * @date 2024/5/23 18:17
 * @description
 **/
@ExtendWith(MockitoExtension.class)
public class EventSessionServiceImplTest {

    @InjectMocks
    private EventSessionServiceImpl eventSessionService;

    @Mock
    private EventSessionMapper eventSessionMapper;

    @Mock
    private SeatMapMapper seatMapMapper;

    @Mock
    private SeatMapper seatMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(eventSessionService, "baseMapper", eventSessionMapper);
    }

    @Test
    @DisplayName("Should insert event session with valid data")
    public void shouldInsertEventSessionWithValidData() {
        int eventId = 1;
        JSONObject sessionData = new JSONObject();
        sessionData.put("registration_required", true);

        JSONArray registrationTimeRange = new JSONArray();
        registrationTimeRange.add("2023-05-01 10:00:00");
        registrationTimeRange.add("2023-05-02 10:00:00");
        sessionData.put("registration_time_range", registrationTimeRange);

        JSONArray eventTimeRange = new JSONArray();
        eventTimeRange.add("2023-06-01 10:00:00");
        eventTimeRange.add("2023-06-02 10:00:00");
        sessionData.put("event_time_range", eventTimeRange);

        sessionData.put("min_cnt", 10);
        sessionData.put("max_cnt", 100);
        sessionData.put("seat_map_id", 1);
        sessionData.put("venue", "Test Venue");
        sessionData.put("location", "[1,2,3]");
        sessionData.put("additional_information_required", "Test Info");
        sessionData.put("visible", true);

        SeatMap seatMap = new SeatMap();
        seatMap.setId(1);
        when(seatMapMapper.selectById(1)).thenReturn(seatMap);

        when(seatMapMapper.insert(any(SeatMap.class))).thenAnswer(invocation -> {
            SeatMap arg = invocation.getArgument(0);
            arg.setId(2); // Setting new ID
            return 1;
        });

        Seat seat = new Seat();
        seat.setSeatMapId(1);
        when(seatMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(seat));

        assertDoesNotThrow(() -> eventSessionService.insertEventSession(eventId, sessionData));
    }

    @Test
    @DisplayName("Should throw exception for invalid registration time range")
    public void shouldThrowExceptionForInvalidRegistrationTimeRange() {
        int eventId = 1;
        JSONObject sessionData = new JSONObject();
        sessionData.put("registration_required", true);

        JSONArray registrationTimeRange = new JSONArray();
        registrationTimeRange.add("2023-05-01 10:00:00");
        sessionData.put("registration_time_range", registrationTimeRange);

        Exception exception = assertThrows(ServiceException.class, () -> eventSessionService.insertEventSession(eventId, sessionData));

        assertEquals("400", ((ServiceException) exception).getCode());
        assertEquals("Invalid registration_time_range", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for invalid event time range")
    public void shouldThrowExceptionForInvalidEventTimeRange() {
        int eventId = 1;
        JSONObject sessionData = new JSONObject();
        sessionData.put("registration_required", false);  // Add this line

        JSONArray eventTimeRange = new JSONArray();
        eventTimeRange.add("2023-06-01 10:00:00");
        sessionData.put("event_time_range", eventTimeRange);

        Exception exception = assertThrows(ServiceException.class, () -> eventSessionService.insertEventSession(eventId, sessionData));

        assertEquals("400", ((ServiceException) exception).getCode());
        assertEquals("Invalid event_time_range", exception.getMessage());
    }

    @Test
    @DisplayName("Should insert event session with registration not required")
    public void shouldInsertEventSessionWithRegistrationNotRequired() {
        int eventId = 1;
        JSONObject sessionData = new JSONObject();
        sessionData.put("registration_required", false);

        JSONArray eventTimeRange = new JSONArray();
        eventTimeRange.add("2023-06-01 10:00:00");
        eventTimeRange.add("2023-06-02 10:00:00");
        sessionData.put("event_time_range", eventTimeRange);

        sessionData.put("min_cnt", 10);
        sessionData.put("max_cnt", 100);
        sessionData.put("seat_map_id", 1);
        sessionData.put("venue", "Test Venue");
        sessionData.put("location", "[1,2,3]");
        sessionData.put("additional_information_required", "Test Info");
        sessionData.put("visible", true);

        SeatMap seatMap = new SeatMap();
        seatMap.setId(1);
        when(seatMapMapper.selectById(1)).thenReturn(seatMap);

        when(seatMapMapper.insert(any(SeatMap.class))).thenAnswer(invocation -> {
            SeatMap arg = invocation.getArgument(0);
            arg.setId(2); // Setting new ID
            return 1;
        });

        Seat seat = new Seat();
        seat.setSeatMapId(1);
        when(seatMapper.selectList(any(QueryWrapper.class))).thenReturn(List.of(seat));

        assertDoesNotThrow(() -> eventSessionService.insertEventSession(eventId, sessionData));
    }

    @Test
    @DisplayName("Should parse date time correctly")
    public void shouldParseDateTimeCorrectly() {
        String dateTimeStr = "2023-05-01 10:00:00";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 5, 1, 10, 0, 0);
        LocalDateTime parsedDateTime = ReflectionTestUtils.invokeMethod(eventSessionService, "parseDateTime", dateTimeStr);
        assertEquals(expectedDateTime, parsedDateTime);
    }
}
