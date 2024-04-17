package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.OrderRecord;

import java.util.List;

public interface IEventService extends IService<Event> {


    void insertEventAndSessions(JSONObject eventData);

    JSONArray getAllEvents();
    JSONArray getAuditList(String eventStatus);

    List<EventSession> getEventSessionsByEventId(int userType, Integer eventId);

    void submitBookingData(int userType, String userId, OrderRecord orderRecord);
}
