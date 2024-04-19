package org.cs304.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.service.IOrderRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements IOrderRecordService {

    @Override
    public Object getMyOrderRecordByEventId(String userId, Integer eventId, Integer mode) {
        if ((eventId == null) || (mode == null) || ((mode != 0) && (mode != 1))) {
            throw new ServiceException("400", "Invalid data");
        }
        List<OrderRecord> orderRecordList = baseMapper.selectList(new QueryWrapper<OrderRecord>().eq("user_id", userId).eq("event_id", eventId));
        if (mode == 0) {
            return orderRecordList.stream().map(OrderRecord::getEventSessionId).collect(Collectors.toList());
        }
        else {
            return orderRecordList;
        }
    }
}
