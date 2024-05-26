package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.History;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.HistoryMapper;
import org.cs304.backend.service.IEventSessionService;
import org.cs304.backend.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HistoryServiceImplTest {
    @Mock
    private HistoryMapper historyMapper;
    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private IEventSessionService eventSessionService;
    @Mock
    EventMapper eventMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(historyService, "baseMapper", historyMapper);

    }

    @Test
    @DisplayName("Successfully add event history record")
    public void testAddEventHistory() {
        // Arrange
        // Arrange
        String userId = "user123";
        int eventId = 1;
        int expectedRowsAffected = 1;

        // Stub the insert method to return 1, indicating one row was affected
        when(historyMapper.insert(any(History.class))).thenReturn(expectedRowsAffected);

        // Act
        historyService.addEventHistory(userId, eventId);

        // Assert
        verify(historyMapper).insert(any(History.class));  // Verify that insert was called
        History capturedHistory = captureHistoryInsertArgument();
        assertNotNull(capturedHistory);
        assertEquals(userId, capturedHistory.getUserId());
        assertEquals(eventId, capturedHistory.getEventId());
    }

    private History captureHistoryInsertArgument() {
        ArgumentCaptor<History> argumentCaptor = ArgumentCaptor.forClass(History.class);
        verify(historyMapper).insert(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    @Test
    @DisplayName("Retrieve user history successfully with no duplicates")
    public void testGetAllHistory_Success() {
        // Arrange
        List<History> histories = Arrays.asList(
                new History("user123", 1, LocalDateTime.now()),
                new History("user123", 1, LocalDateTime.now().minusDays(1)),
                new History("user123", 2, LocalDateTime.now().minusDays(2))
        );
        when(historyMapper.selectList(any())).thenReturn(histories);

        Event event1 = new Event();
        event1.setId(1);
        event1.setName("Event One");

        Event event2 = new Event();
        event2.setId(2);
        event2.setName("Event Two");

        when(eventMapper.selectById(1)).thenReturn(event1);
        when(eventMapper.selectById(2)).thenReturn(event2);

        // Act
        JSONArray jsonArray = historyService.getAllHistory("user123");

        // Assert
        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.size()); // Ensure no duplicates and two unique events returned
        verify(historyMapper).selectList(any()); // Verify the correct method was called on the mapper
        verify(eventMapper, times(1)).selectById(1); // Verify event details fetched once for ID 1
        verify(eventMapper, times(1)).selectById(2); // Verify event details fetched once for ID 2

        // Additional checks to verify the content of the JSON objects
        JSONObject firstEvent = (JSONObject) jsonArray.get(1);
        assertEquals("Event Two", firstEvent.getString("name")); // Assuming the events are sorted by visit time desc
        JSONObject secondEvent = (JSONObject) jsonArray.get(0);
        assertEquals("Event One", secondEvent.getString("name"));
    }
}
