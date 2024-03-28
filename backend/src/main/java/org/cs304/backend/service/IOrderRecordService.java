package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.OrderRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IOrderRecordService extends IService<OrderRecord> {

    Object getMyOrderRecordByEventId(String userId, Integer eventId, Integer mode);
}
