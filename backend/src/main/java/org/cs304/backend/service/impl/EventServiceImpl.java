package org.cs304.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.Event;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.service.IEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {
    @Autowired
    private EventMapper eventMapper;

    // 插入新事件
    public void insertEvent(Event event) {
        eventMapper.insert(event);
    }

    // 更新事件
    public void updateEvent(Event event) {
        eventMapper.updateById(event);
    }

    // 删除事件
    public void deleteEvent(Long eventId) {
        eventMapper.deleteById(eventId);
    }

    // 查询事件
    public Event getEventById(Long eventId) {
        return eventMapper.selectById(eventId);
    }

    // 根据条件查询事件列表
    public List<Event> listEvents(QueryWrapper<Event> wrapper) {
        return eventMapper.selectList(wrapper);
    }
}
