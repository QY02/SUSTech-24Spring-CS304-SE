package org.cs304.backend.service;

import org.cs304.backend.entity.SeatMap;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISeatMapService extends IService<SeatMap> {
SeatMap getSeatMapWithSeatsById(int userType, Integer seatMapId);
}
