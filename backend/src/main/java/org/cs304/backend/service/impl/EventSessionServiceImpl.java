package org.cs304.backend.service.impl;

import org.cs304.backend.entity.EventSession;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.service.IEventSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EventSessionServiceImpl extends ServiceImpl<EventSessionMapper, EventSession> implements IEventSessionService {

}
