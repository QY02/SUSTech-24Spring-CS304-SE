package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.service.IEventSessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventSessionServiceImpl extends ServiceImpl<EventSessionMapper, EventSession> implements IEventSessionService {
    @Resource
    private EventSessionMapper eventSessionMapper;

    @Override
    public void insertEventSession(int id,JSONObject sessionData){
        EventSession session = new EventSession();

        // 设置 event_id
        session.setEventId(id);

        // 设置 registration_required
        Boolean registration_required=sessionData.getBoolean("registration_required");
        session.setRegistrationRequired(registration_required);
        if (registration_required){
            // 设置 registration_start_time 和 registration_end_time
            JSONArray registrationTimeRange = sessionData.getJSONArray("registration_time_range");
            LocalDateTime registrationStartTime = parseDateTime(registrationTimeRange.getString(0));
            LocalDateTime registrationEndTime = parseDateTime(registrationTimeRange.getString(1));
            session.setRegistrationStartTime(registrationStartTime);
            session.setRegistrationEndTime(registrationEndTime);
        }


        // 设置 start_time 和 end_time
        JSONArray eventTimeRange = sessionData.getJSONArray("event_time_range");
        LocalDateTime startTime = parseDateTime(eventTimeRange.getString(0));
        LocalDateTime endTime = parseDateTime(eventTimeRange.getString(1));
        session.setStartTime(startTime);
        session.setEndTime(endTime);

        // 设置 min_size 和 max_size
        JSONArray countRangeOfPeople = sessionData.getJSONArray("count_range_of_people");
        int minSize = countRangeOfPeople.getInteger(0);
        int maxSize = countRangeOfPeople.getInteger(1);
        session.setMinSize(minSize);
        session.setMaxSize(maxSize);

        session.setCurrentSize(sessionData.getInteger("current_size"));
        session.setSeatMapId(sessionData.getInteger("seat_map_id"));
        session.setVenue(sessionData.getString("venue"));
        session.setLocation(sessionData.getString("location"));
        session.setAdditionalInformationRequired(sessionData.getString("additional_information_required"));
        session.setVisible(sessionData.getBoolean("visible"));

        // 打印 Event 对象的属性值
//        System.out.println("Session Object: " + session);

        eventSessionMapper.insert(session);
    }
    private LocalDateTime parseDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}


