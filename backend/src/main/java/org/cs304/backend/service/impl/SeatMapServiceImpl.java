package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_EventStatus;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.Seat;
import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.SeatMapMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.ISeatMapService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SeatMapServiceImpl extends ServiceImpl<SeatMapMapper, SeatMap> implements ISeatMapService {
    @Resource
    private EventMapper eventMapper;

    @Resource
    private EventSessionMapper eventSessionMapper;

    @Resource
    private SeatMapper seatMapper;

    @Override
    public SeatMap getSeatMapWithSeatsById(int userType, Integer seatMapId) {
        SeatMap seatMap = baseMapper.selectById(seatMapId);
        if (seatMap == null) {
            throw new ServiceException("400", "Seat map not exist");
        }
        if ((userType == constant_User.USER) && (seatMap.getType() == 1)) {
            List<Integer> eventIdList = eventSessionMapper.selectList(new QueryWrapper<EventSession>().eq("seat_map_id", seatMapId).eq("visible", true)).stream().map(EventSession::getEventId).collect(Collectors.toList());
            if (eventIdList.isEmpty()) {
                throw new ServiceException("400", "Seat map not exist");
            } else if (!eventMapper.exists(new QueryWrapper<Event>().in("id", eventIdList).eq("status", constant_EventStatus.PASSED).eq("visible", true))) {
                throw new ServiceException("400", "Seat map not exist");
            }
        }
        JSONObject seatMapData = JSONObject.parseObject(seatMap.getData());
        JSONArray seatDataJSONArray = seatMapData.getJSONArray("seats");
        List<String> seatIdList = seatDataJSONArray.stream().map(seat -> ((JSONObject) seat).getString("id")).collect(Collectors.toList());
        Map<String, Seat> seatDetailedDataMap = seatMapper.selectList(new QueryWrapper<Seat>().in("seat_id", seatIdList).eq("seat_map_id", seatMapId)).stream().collect(Collectors.toMap(Seat::getSeatId, seat -> seat));
        seatDataJSONArray.forEach(seat -> {
            JSONObject seatDataJSONObject = (JSONObject) seat;
            Seat seatDetailedData = seatDetailedDataMap.get(seatDataJSONObject.getString("id"));
            seatDataJSONObject.put("type", seatDetailedData.getType());
            seatDataJSONObject.put("availability", seatDetailedData.getAvailability());
            seatDataJSONObject.put("price", seatDetailedData.getPrice());
        });
        seatMap.setData(null);
        seatMap.setDetailedData(seatMapData);
        return seatMap;
    }
}
