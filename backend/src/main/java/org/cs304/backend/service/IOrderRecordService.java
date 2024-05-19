package org.cs304.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.Favorite;
import org.cs304.backend.entity.OrderRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOrderRecordService extends IService<OrderRecord> {

    Object getMyOrderRecord(String userId, Integer eventId, Integer mode);

    Object getUnpaidOrderRecord(String userId, Integer eventId, Integer mode);

    int getPaymentById(Integer orderId);

}
