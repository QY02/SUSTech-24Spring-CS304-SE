package org.cs304.backend.service.impl;

import org.cs304.backend.entity.SeatMap;
import org.cs304.backend.mapper.SeatMapMapper;
import org.cs304.backend.service.ISeatMapService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SeatMapServiceImpl extends ServiceImpl<SeatMapMapper, SeatMap> implements ISeatMapService {

}
