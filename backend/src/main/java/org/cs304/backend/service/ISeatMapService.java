package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import org.cs304.backend.entity.SeatMap;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISeatMapService extends IService<SeatMap> {

    SeatMap getSeatMapWithSeatsById(int userType, Integer seatMapId);

    JSONArray getAllSeatMapTemplate();
}
