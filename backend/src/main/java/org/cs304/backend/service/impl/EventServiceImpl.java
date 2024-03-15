package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.service.IEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cs304.backend.service.IEventSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {

    @Resource
    private IEventSessionService eventSessionService;

    @Override
    public JSONArray getAuditList() {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>().eq("status", constant_EventStatus.AUDITING.getValue());
        List<Event> list = list(queryWrapper);
        if (list != null) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(list);
            for (int i = 0; i < jsonArray.size(); i++) {
                QueryWrapper<EventSession> queryWrapper1 = new QueryWrapper<EventSession>().eq("event_id", jsonArray.getJSONObject(i).getInteger("id"));
                List<EventSession> eventSessionList = eventSessionService.list(queryWrapper1);
                LocalDateTime start_time = eventSessionList.get(0).getStartTime();
                for (int j = 1; j < eventSessionList.size(); j++) {
                    if (start_time.isAfter(eventSessionList.get(j).getStartTime())) {
                        start_time = eventSessionList.get(j).getStartTime();
                    }
                }
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.put("startTime", start_time);
                jsonObject.put("location", eventSessionList.get(0).getLocation());
                jsonArray.set(i, jsonObject);
            }
            return jsonArray;
        }
        return new JSONArray();
    }
}
