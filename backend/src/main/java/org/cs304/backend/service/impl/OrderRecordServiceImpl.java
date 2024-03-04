package org.cs304.backend.service.impl;

import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.mapper.OrderRecordMapper;
import org.cs304.backend.service.IOrderRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements IOrderRecordService {

}
