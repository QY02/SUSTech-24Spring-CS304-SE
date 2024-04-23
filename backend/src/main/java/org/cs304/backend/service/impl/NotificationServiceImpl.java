package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_NotificationStatus;
import org.cs304.backend.constant.constant_NotificationType;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.EventSession;
import org.cs304.backend.entity.Notification;
import org.cs304.backend.entity.User;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.NotificationMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.INotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author zyp
 * @date 2024/4/21 0:36
 * @description
 **/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EventMapper eventMapper;

    @Resource
    private EventSessionMapper eventSessionMapper;

    @Resource
    private EmailService emailService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss EEEE", Locale.CHINESE);

    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    private void insertImmediateNotification(String publisherId, String notifiedUserId, String title, String content,int type) {
        insertNotification(publisherId, notifiedUserId, title, content, new Date(),type);
    }

    private void insertNotification(String publisherId, String notifiedUserId, String title, String content, Date date,int type) {
        User user = userMapper.selectById(notifiedUserId);
        if (user == null) {
            throw new ServiceException("User not found");
        } else {
            String toEmail = user.getEmail();
            Notification notification = new Notification();
            notification.setStatus(constant_NotificationStatus.UNREAD);
            notification.setType(type);
            notification.setNotifiedUserId(notifiedUserId);
            notification.setPublisherId(publisherId);
            notification.setCreateTime(date);
            notification.setNotifyTime(date);
            notification.setTitle(title);
            notification.setContent(content);

            notificationMapper.insert(notification);
            emailService.sendEmail(toEmail, title, content, date);
        }
    }
    private JSONArray convertNotificationListToJsonArray(List<Notification> notificationList) {
        if (notificationList != null && !notificationList.isEmpty()) {
            // 将查询到的数据列表转换为 JSONArray
            return (JSONArray) JSON.toJSON(notificationList);
        } else {
            // 返回空的 JSONArray，表示没有找到符合条件的通知
            return new JSONArray();
        }
    }

    @Override
    public void insertEventPassNotification(String publisherId, int eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new ServiceException("Event not found");
        } else {
            String notifiedUserId = event.getPublisherId();
            String eventTitle = event.getName();
            String title = "活动申请通过";
            String content = String.format("活动'%s'申请已通过！。", eventTitle);
            insertImmediateNotification(publisherId, notifiedUserId, title, content, constant_NotificationType.PASS);
        }
    }

    @Override
    public void insertEventNotPassNotification(String publisherId, int eventId, String comment) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new ServiceException("Event not found");
        } else {
            String notifiedUserId = event.getPublisherId();
            String eventTitle = event.getName();
            String title = "活动申请未通过";
            String content = String.format("您好！\n很遗憾地通知您：您申请的活动'%s'申请未通过。\n\n审核意见如下：\n%s", eventTitle, comment);
            insertImmediateNotification(publisherId, notifiedUserId, title, content,constant_NotificationType.NOTPASS);
        }
    }

    @Override
    public void insertReserveSessionNotification(String notifiedUserId, int sessionId) {
        EventSession eventSession = eventSessionMapper.selectById(sessionId);
        if (eventSession == null) {
            throw new ServiceException("Event session not found");
        } else {
            int eventId = eventSession.getEventId();
            Event event = eventMapper.selectById(eventId);
            if (event == null) {
                throw new ServiceException("Event not found");
            } else {
                String publisherId = event.getPublisherId();
                String eventTitle = event.getName();
                LocalDateTime startTime = eventSession.getStartTime();
                LocalDateTime endTime = eventSession.getEndTime();
                String title = "成功参加活动";
                String content = String.format("您好！\n您已成功参加活动'%s'的'%s ~ %s'场次。", eventTitle, formatDateTime(startTime), formatDateTime(endTime));
                insertImmediateNotification(publisherId, notifiedUserId, title, content,constant_NotificationType.RESERVE);
            }
        }
    }

    @Override
    public void insertAdminNotification(String publishId, String notifiedUserId, String title, String content) {
        insertImmediateNotification(publishId, notifiedUserId, title, content,constant_NotificationType.OTHER);
    }

    @Override
    public void insertEventModifyNotification(String publisherId, int eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new ServiceException("Event not found");
        } else {
            String notifiedUserId = event.getPublisherId();
            String eventTitle = event.getName();
            String title = "活动修改通知";
            String content = String.format("您好！\n您参加的活动'%s'信息发生了改变！", eventTitle);
            insertImmediateNotification(publisherId, notifiedUserId, title, content,constant_NotificationType.MODIFY);
        }
    }

    @Override
    public void insertEventCancelNotification(String userId, int sessionId, String comment) {
        EventSession eventSession = eventSessionMapper.selectById(sessionId);
        if (eventSession == null) {
            throw new ServiceException("Event session not found");
        } else {
            int eventId = eventSession.getEventId();
            Event event = eventMapper.selectById(eventId);
            if (event == null) {
                throw new ServiceException("Event not found");
            } else {
                String publisherId = event.getPublisherId();
                String eventTitle = event.getName();
                LocalDateTime startTime = eventSession.getStartTime();
                LocalDateTime endTime = eventSession.getEndTime();
                String title = "活动取消通知";
                String content = String.format("您好！\n遗憾地通知您：您参加的活动'%s'的‘%s ~ %s'场次取消。\n\n具体原因如下：\n%s",
                        eventTitle, formatDateTime(startTime), formatDateTime(endTime), comment);
                insertImmediateNotification(publisherId, userId, title, content,constant_NotificationType.CANCEL);
            }
        }
    }

    @Override
    public void updateReadStatus(int notificationId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            throw new ServiceException("Notification not found.");
        } else {
//            // 构造更新条件，这里假设按照通知的ID进行更新
//            UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("id", notificationId);
//
//            // 设置要更新的字段和值，这里更新状态为已读
//            Notification updateEntity = new Notification();
//            updateEntity.setStatus(constant_NotificationStatus.READ);
//
//            // 执行更新操作，只更新指定字段
//            int rowsAffected = notificationMapper.update(updateEntity, updateWrapper);
//            if (rowsAffected <= 0) {
//                throw new ServiceException("Failed to update notification status.");
//            }
            notification.setStatus(constant_NotificationStatus.UNREAD);
            notificationMapper.updateById(notification);
        }
    }

    @Override
    public JSONArray getAllNotificationsOfOneUser(String userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notificationList = notificationMapper.selectList(new QueryWrapper<Notification>().eq("notified_user_id", userId)
                .lt("notify_time", now)
                .orderByDesc("notify_time"));
        return convertNotificationListToJsonArray(notificationList);
    }

    @Override
    public JSONArray getNotificationsOfOneUserByPage(String userId, int pageNum, int pageSize) {
        LocalDateTime now = LocalDateTime.now();
        // 创建分页对象，指定当前页和每页记录数
        Page<Notification> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("notified_user_id", userId)
                .lt("notify_time", now)
                .orderByDesc("notify_time");

        // 执行分页查询
        IPage<Notification> notificationPage = notificationMapper.selectPage(page, queryWrapper);

        List<Notification> notificationList = notificationPage.getRecords();

        return convertNotificationListToJsonArray(notificationList);
    }
}
