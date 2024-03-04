package org.cs304.backend.service.impl;

import org.cs304.backend.entity.Event;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.service.IEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {

}
