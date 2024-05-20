package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.exception.ServiceException;
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
//        System.out.println("sessionData in method");
//        System.out.println(sessionData);
        EventSession session = new EventSession();

        // 设置 event_id
        session.setEventId(id);

        // 设置 registration_required
        Boolean registration_required=sessionData.getBoolean("registration_required");
        session.setRegistrationRequired(registration_required);
        if (registration_required){
            // 设置 registration_start_time 和 registration_end_time
            JSONArray registrationTimeRange = sessionData.getJSONArray("registration_time_range");
            if (registrationTimeRange.size()==2){
                LocalDateTime registrationStartTime = parseDateTime(registrationTimeRange.getString(0));
                LocalDateTime registrationEndTime = parseDateTime(registrationTimeRange.getString(1));
                session.setRegistrationStartTime(registrationStartTime);
                session.setRegistrationEndTime(registrationEndTime);
            }else {
                throw new ServiceException("400", "Invalid registration_time_range");
            }
        }


        // 设置 start_time 和 end_time
        JSONArray eventTimeRange = sessionData.getJSONArray("event_time_range");
        if (eventTimeRange.size()==2){
            LocalDateTime startTime = parseDateTime(eventTimeRange.getString(0));
            LocalDateTime endTime = parseDateTime(eventTimeRange.getString(1));
            session.setStartTime(startTime);
            session.setEndTime(endTime);
        }else {
            throw new ServiceException("400", "Invalid event_time_range");
        }


        // 设置 min_size 和 max_size
        int minSize = sessionData.getInteger("min_cnt");
        int maxSize = sessionData.getInteger("max_cnt");
        session.setMinSize(minSize);
        session.setMaxSize(maxSize);

        session.setCurrentSize(0);
        // TODO:setSeatMapId,这里应该要给真实的数据
        session.setSeatMapId(1);
        session.setVenue(sessionData.getString("venue"));
        session.setLocation(sessionData.getString("location"));
        JSONArray addi_info_arr=sessionData.getJSONArray("additional_information_required");
        StringBuilder addi_info_sb = new StringBuilder();
        addi_info_sb.append("[");
        for (int i = 0; i < addi_info_arr.size(); i++) {
            if (i > 0) {
                addi_info_sb.append(", ");
            }
            addi_info_sb.append(addi_info_arr.getString(i));
        }
        addi_info_sb.append("]");
//        System.out.println("addi_info_str");
        String addi_info_str=String.valueOf(addi_info_sb);
//        System.out.println(addi_info_str);

        session.setAdditionalInformationRequired(addi_info_str);

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


