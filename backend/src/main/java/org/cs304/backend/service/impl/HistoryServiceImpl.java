package org.cs304.backend.service.impl;

import org.cs304.backend.entity.History;
import org.cs304.backend.mapper.HistoryMapper;
import org.cs304.backend.service.IHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements IHistoryService {

}
