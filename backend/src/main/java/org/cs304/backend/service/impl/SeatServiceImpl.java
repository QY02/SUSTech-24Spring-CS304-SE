package org.cs304.backend.service.impl;

import org.cs304.backend.entity.Seat;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.ISeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements ISeatService {

}