package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.Notification;

import java.util.Date;

/**
 * @author zyp
 * @date 2024/4/21 0:35
 * @description
 **/
public interface INotificationService extends IService<Notification> {

    void insertEventPassNotification(String publisherId, int eventId);

    void insertEventNotPassNotification(String publisherId, int eventId, String comment);

    void insertReserveSessionNotification(String notifiedUserId, int sessionId);

    void insertAdminNotification(String publishId, String notifiedUserId, String title, String content);

    void insertEventModifyNotification(String userId, int eventId);

    void insertEventCancelNotification(String userId, int eventId, String comment);

    void updateReadStatus(int notificationId);
}
