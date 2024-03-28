package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import org.cs304.backend.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.OrderRecord;

import java.util.List;

public interface IEventService extends IService<Event> {

    JSONArray getAuditList();

    List<EventSession> getEventSessionsByEventId(int userType, Integer eventId);

    void submitBookingData(int userType, String userId, OrderRecord orderRecord);
}
