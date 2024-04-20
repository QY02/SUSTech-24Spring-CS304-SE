package org.cs304.backend.service;

import org.cs304.backend.entity.History;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.OrderRecord;

import java.time.LocalDateTime;

public interface IHistoryService extends IService<History> {
    void addEventHistory(String userId, int eventId);

}
