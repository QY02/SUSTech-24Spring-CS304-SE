package org.cs304.backend.service;

import org.cs304.backend.entity.OrderRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOrderRecordService extends IService<OrderRecord> {

    Object getMyOrderRecord(String userId, Integer eventId, Integer mode);
}
