package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.controller.HistoryController;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.History;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.HistoryMapper;
import org.cs304.backend.service.IHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements IHistoryService {
    @Resource
    private HistoryMapper historyMapper;
    @Override
    public void addEventHistory(String userId, int eventId) {
        History history=new History();
        history.setEventId(eventId);
        history.setUserId(userId);
        history.setVisitTime(LocalDateTime.now());
//        System.out.println(history.toString());

        // 打印 Event 对象的属性值
//        System.out.println("Event Object: " + event);
        historyMapper.insert(history);
    }
}
