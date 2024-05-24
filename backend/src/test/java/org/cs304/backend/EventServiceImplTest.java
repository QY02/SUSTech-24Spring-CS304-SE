package org.cs304.backend;

/**
 * @author zyp
 * @date 2024/5/23 19:17
 * @description
 **/

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.*;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.*;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.IEventSessionService;
import org.cs304.backend.service.INotificationService;
import org.cs304.backend.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private IEventSessionService eventSessionService;
    @Mock
    private IAttachmentService attachmentService;
    @Mock
    EventMapper eventMapper;
    @Mock
    private EventSessionMapper eventSessionMapper;
    @Mock
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;
    @Mock
    private SeatMapper seatMapper;
    @Mock
    private OrderRecordMapper orderRecordMapper;
    @Mock
    private UserInteractionMapper userInteractionMapper;
    @Mock
    private UserFavoriteTypeMapper userFavoriteTypeMapper;
    @Mock
    private HistoryMapper historyMapper;
    @Mock
    private INotificationService notificationService;
    @Mock
    private Lock submitBookingDataLock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(eventService, "baseMapper", eventMapper);

    }


    @Test
    @DisplayName("Should get attachment for given event ID")
    public void shouldGetAttachment() {
        int eventId = 1;
        EntityAttachmentRelation relation = new EntityAttachmentRelation();
        relation.setAttachmentId(1);
        when(entityAttachmentRelationMapper.selectOne(any())).thenReturn(relation);

        Attachment attachment = new Attachment();
        attachment.setFilePath("path/to/attachment");
        when(attachmentService.getById(anyInt(), anyInt())).thenReturn(attachment);

        String result = eventService.getAttachment(eventId);
        assertEquals("path/to/attachment", result);
    }

    @Test
    @DisplayName("Should create event start and return JSON object")
    public void shouldCreateEventStart() {
        JSONObject data = new JSONObject();
        JSONObject eventData = new JSONObject();
        eventData.put("name", "Test Event");
        data.put("event", eventData);

        JSONArray sessionData = new JSONArray();
        data.put("sessions", sessionData);

        JSONArray posterData = new JSONArray();
        posterData.add("poster1");
        data.put("poster", posterData);

        when(attachmentService.uploadBatchStart(anyInt(), anyList(), anyList(), any())).thenReturn(new JSONObject());

        JSONObject result = eventService.createEventStart(data);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should create event finish and return event ID")
    public void shouldCreateEventFinish() {
        JSONObject requestData = new JSONObject();
        Event event = new Event();
        event.setId(1);
        requestData.put("id", event.getId());

        JSONArray sessionData = new JSONArray();
        JSONObject session1 = new JSONObject();
        session1.put("sessionId", 1);
        sessionData.add(session1);
        requestData.put("eventSessionData", sessionData);

        List<Attachment> attachmentList = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setId(1);
        attachmentList.add(attachment);
        requestData.put("fileInfoList", attachmentList);

        when(eventMapper.insert(any(Event.class))).thenAnswer(invocation -> {
            Event arg = invocation.getArgument(0);
            arg.setId(1);  // Set ID after insert
            return 1;
        });

        JSONObject result = eventService.createEventFinish(requestData);
        assertNotNull(result);
        assertEquals(1, result.getInteger("eventId"));
    }

    @Test
    @DisplayName("Should throw exception for invalid event session data")
    public void shouldThrowExceptionForInvalidEventSessionData() {
        int eventId = 1;
        JSONArray sessionData = new JSONArray();

        Exception exception = assertThrows(ServiceException.class, () -> {
            ReflectionTestUtils.invokeMethod(eventService, "insertSessions", eventId, sessionData);
        });

        assertEquals("eventSessionData is null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should get event sessions by event ID")
    public void shouldGetEventSessionsByEventId() {
        int userType = 1;
        int eventId = 1;
        Event event = new Event();
        event.setId(eventId);
        event.setStatus(1);
        event.setVisible(true);

        when(eventMapper.selectById(eventId)).thenReturn(event);

        List<EventSession> sessionList = new ArrayList<>();
        EventSession session = new EventSession();
        session.setEventId(eventId);
        sessionList.add(session);

        when(eventSessionMapper.selectList(any())).thenReturn(sessionList);

        List<EventSession> result = eventService.getEventSessionsByEventId(userType, eventId);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should submit booking data successfully")
    public void shouldSubmitBookingData() {
        int userType = 1;
        String userId = "user123";
        OrderRecord orderRecord = new OrderRecord();
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

        assertDoesNotThrow(() -> eventService.submitBookingData(userType, userId, orderRecord));
    }

    @Test
    @DisplayName("Should get all events ordered by publish date")
    public void shouldGetAllEvents() {
        List<Event> eventList = new ArrayList<>();
        Event event = new Event();
        event.setId(1);
        eventList.add(event);

        when(eventMapper.selectList(any())).thenReturn(eventList);

        JSONArray result = eventService.getAllEvents();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should change audit status and notify")
    public void shouldChangeAudit() {
        Integer eventId = 1;
        Integer status = 1;
        String reason = "Reason";
        String publisherId = "publisher123";

        Event event = new Event();
        event.setId(eventId);
        when(eventMapper.selectById(eventId)).thenReturn(event);

        doNothing().when(notificationService).insertEventPassNotification(anyString(), anyInt());
        doNothing().when(notificationService).insertEventNotPassNotification(anyString(), anyInt(), anyString());

        assertDoesNotThrow(() -> eventService.changeAudit(publisherId, eventId, status, reason));
    }

}