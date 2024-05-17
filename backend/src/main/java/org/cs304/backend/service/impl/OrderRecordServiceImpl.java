package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.service.IOrderRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements IOrderRecordService {

    @Resource
    private EventMapper eventMapper;
    @Resource
    private EventSessionMapper eventSessionMapper;

    @Override
    public Object getMyOrderRecord(String userId, Integer eventId, Integer mode) {
        if ((mode == null) || ((mode != 0) && (mode != 1) && (mode != 2) && (mode != 3))) {
            throw new ServiceException("400", "Invalid data");
        }
        QueryWrapper<OrderRecord> queryWrapper = new QueryWrapper<OrderRecord>().eq("user_id", userId);
        if (eventId != null) {
            queryWrapper.eq("event_id", eventId);
        }
        List<OrderRecord> orderRecordList = baseMapper.selectList(queryWrapper);
        return orderRecordList.stream().map(orderRecord -> {
            if (mode == 0) {
                return orderRecord.getEventSessionId();
            } else if (mode == 1) {
                return orderRecord;
            } else {
                JSONObject jsonObject = JSONObject.from(orderRecord);
                jsonObject.put("eventSession", eventSessionMapper.selectById(orderRecord.getEventSessionId()));
                if (mode == 3) {
                    jsonObject.put("event", eventMapper.selectById(orderRecord.getEventId()));
                }
                return jsonObject;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public int getPaymentById(Integer orderId) {
        if (orderId == null) {
            throw new ServiceException("400", "Invalid data");
        }
        QueryWrapper<OrderRecord> queryWrapper = new QueryWrapper<OrderRecord>().eq("id", orderId);
        OrderRecord orderRecord = baseMapper.selectOne(queryWrapper);
        if(orderRecord.getPaymentTime()!=null){
            return 1;
        }
        return 0;
    }
}
