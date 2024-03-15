package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONArray;
import org.cs304.backend.entity.Event;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IEventService extends IService<Event> {

    JSONArray getAuditList();
}
