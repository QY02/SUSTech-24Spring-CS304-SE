package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.service.IEventSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {

    @Resource
    private IEventSessionService eventSessionService;
    @Resource
    private EventSessionMapper eventSessionMapper;

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
}
