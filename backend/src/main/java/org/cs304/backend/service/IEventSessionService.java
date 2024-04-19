package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.EventSession;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IEventSessionService extends IService<EventSession> {

    void insertEventSession(int id, JSONObject sessionData);
}
