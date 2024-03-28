package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.constant.constant_OrderRecordStatus;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.service.IEventSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {

    @Resource
    private IEventSessionService eventSessionService;
    @Resource
    private EventSessionMapper eventSessionMapper;
    @Resource
    private SeatMapper seatMapper;
    @Resource
    private OrderRecordMapper orderRecordMapper;

    @Override
    public JSONArray getAuditList() {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>().eq("status", constant_EventStatus.AUDITING);
        List<Event> list = list(queryWrapper);
        if (list != null) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(list.stream().map(JSON::toJSON).toList());
            for (int i = 0; i < jsonArray.size(); i++) {
                QueryWrapper<EventSession> queryWrapper1 = new QueryWrapper<EventSession>().eq("event_id", jsonArray.getJSONObject(i).getInteger("id"));
                List<EventSession> eventSessionList = eventSessionService.list(queryWrapper1);
                if (eventSessionList == null || eventSessionList.isEmpty()) {
                    jsonArray.getJSONObject(i).put("startTime", null);
                    jsonArray.getJSONObject(i).put("location", null);
                    continue;
                }
                LocalDateTime start_time = eventSessionList.get(0).getStartTime();
                for (int j = 1; j < eventSessionList.size(); j++) {
                    if (start_time.isAfter(eventSessionList.get(j).getStartTime())) {
                        start_time = eventSessionList.get(j).getStartTime();
                    }
                }
                jsonArray.getJSONObject(i).put("startTime", start_time);
                jsonArray.getJSONObject(i).put("location", eventSessionList.get(0).getLocation());
            }
            return jsonArray;
        }
        return new JSONArray();
    }

    @Override
    public List<EventSession> getEventSessionsByEventId(int userType, Integer eventId) {
        if (eventId == null) {
            throw new ServiceException("400", "Invalid event id");
        }
        Event event = baseMapper.selectById(eventId);
        if (event == null) {
            throw new ServiceException("400", "Event not exist");
        }
        if ((userType != constant_User.ADMIN) && ((event.getStatus() != constant_EventStatus.PASSED) || (!event.getVisible()))) {
            throw new ServiceException("400", "Event not exist");
        }
        return eventSessionMapper.selectList(new QueryWrapper<EventSession>().eq("event_id", eventId)).stream().filter(eventSession -> (userType == constant_User.ADMIN) || (eventSession.getVisible())).collect(Collectors.toList());
    }

    @Override
    public void submitBookingData(int userType, String userId, OrderRecord orderRecord) {
        Event event = baseMapper.selectById(orderRecord.getEventId());
        if ((event == null) || (event.getStatus() != constant_EventStatus.PASSED) || (!event.getVisible())) {
            throw new ServiceException("400", "Event not exist");
        }
        EventSession eventSession = eventSessionMapper.selectById(orderRecord.getEventSessionId());
        if ((eventSession == null) || (!eventSession.getVisible()) || (!Objects.equals(eventSession.getEventId(), orderRecord.getEventId()))) {
            throw new ServiceException("400", "Event session not exist");
        }
        if (!eventSession.getRegistrationRequired()) {
            throw new ServiceException("401", "This event session does not require registration");
        }
        if (LocalDateTime.now().isBefore(eventSession.getRegistrationStartTime()) || (!LocalDateTime.now().isBefore(eventSession.getRegistrationEndTime()))) {
            throw new ServiceException("401", "Not within the registration time period");
        }
        if (eventSession.getCurrentSize() >= eventSession.getMaxSize()) {
            throw new ServiceException("401", "This session is full");
        }
        Seat seat = seatMapper.selectOne(new QueryWrapper<Seat>().eq("seat_map_id", eventSession.getSeatMapId()).eq("seat_id", orderRecord.getSeatId()));
        if (seat == null) {
            throw new ServiceException("400", "Seat not exist");
        }
        if (!seat.getAvailability()) {
            throw new ServiceException("401", "The seat is unavailable");
        }
        if (orderRecordMapper.exists(new QueryWrapper<OrderRecord>().eq("user_id", userId).eq("event_id", orderRecord.getEventId()).eq("event_session_id", orderRecord.getEventSessionId()))) {
            throw new ServiceException("400", "The order record already exist");
        }
        orderRecord.setUserId(userId);
        orderRecord.setPrice(seat.getPrice());
        orderRecord.setStatus(constant_OrderRecordStatus.SUBMITTED);
        orderRecord.setSubmitTime(LocalDateTime.now());
        orderRecord.setPaymentTime(null);
        orderRecordMapper.insert(orderRecord);
    }
}
