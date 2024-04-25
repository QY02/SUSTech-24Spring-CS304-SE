package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Notification;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.NotificationMapper;
import org.cs304.backend.service.INotificationService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author zyp
 * @date 2024/4/21 0:32
 * @description
 **/
@Slf4j
@RestController
@RequestMapping("/notification")
public class NotificationController {
    @Resource
    private INotificationService notificationService;
    @Resource
    private NotificationMapper notificationMapper;

    @PostMapping("/eventPass/{eventId}")
    @Operation(summary = "发送申请活动成功通知", description = "传入eventId")
    public Result postEventPass(HttpServletResponse response, HttpServletRequest request, @PathVariable int eventId) {
        String userId = (String) request.getAttribute("loginUserId");
        notificationService.insertEventPassNotification(userId, eventId);
        return Result.success(response);
    }


    @PostMapping("/eventNotPass/{eventId}")
    @Operation(summary = "发送申请活动失败通知", description = "传入eventId和反馈comment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "comment": "反馈"
            }
            """)))
    public Result postEventNotPass(HttpServletResponse response, HttpServletRequest request, @PathVariable int eventId, @RequestBody JSONObject data) {
        String userId = (String) request.getAttribute("loginUserId");
        notificationService.insertEventNotPassNotification(userId, eventId, data.getString("comment"));
        return Result.success(response);
    }


    @PostMapping("/eventCancel/{eventId}")
    @Operation(summary = "发送活动取消通知(别用)", description = "传入eventId和取消原因comment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "comment": "取消原因"
            }
            """)))
    public Result postEvenCancel(HttpServletResponse response, HttpServletRequest request, @PathVariable int eventId, @RequestBody JSONObject data) {
        String userId = (String) request.getAttribute("loginUserId");
        notificationService.insertEventCancelNotification(userId, eventId, data.getString("comment"));
        return Result.success(response);
    }


    @PostMapping("/eventModify/{eventId}")
    @Operation(summary = "发送申请修改通知(别用)", description = "传入eventId")
    public Result postEventModify(HttpServletResponse response, HttpServletRequest request, @PathVariable int eventId) {
        String userId = (String) request.getAttribute("loginUserId");
        notificationService.insertEventModifyNotification(userId, eventId);
        return Result.success(response);
    }


    @PostMapping("/reserveSession/{eventSessionId}")
    @Operation(summary = "发送预定场次成功通知", description = "传入eventSessionId")
    public Result postReserveSession(HttpServletResponse response, HttpServletRequest request, @PathVariable int eventSessionId) {
        String userId = (String) request.getAttribute("loginUserId");
        notificationService.insertReserveSessionNotification(userId, eventSessionId);
        return Result.success(response);
    }


    @PostMapping("/admin/{notifiedUserId}")
    @Operation(summary = "管理员发送通知", description = "(1)传入notifiedUserId;\n(2)传入包含title和content的RequestBody")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "title":"标题",
              "content":"内容"
            }""")))
    public Result postAdminNotification(HttpServletResponse response, HttpServletRequest request, @PathVariable String notifiedUserId, @RequestBody JSONObject data) {
        String publishId = (String) request.getAttribute("loginUserId");
        String title = data.getString("title");
        String content = data.getString("content");
        notificationService.insertAdminNotification(publishId, notifiedUserId, title, content);
        return Result.success(response);
    }

    @DeleteMapping("/delete/{notificationId}")
    @Operation(summary = "删除指定通知", description = "传入notifiedUserId,管理员可以删除任意人的通知，普通用户只能删除自己的通知")
    public Result deleteMyNotification(HttpServletResponse response, HttpServletRequest request, @PathVariable String notificationId) {
        String uid = (String) request.getAttribute("loginUserId");
        int userType = (int) request.getAttribute("loginUserType");
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            log.error("Notification not exist");
            return Result.error(response, "401", "Notification not exist");
        }
        if (userType != constant_User.ADMIN && !Objects.equals(uid, notification.getNotifiedUserId())) {
            log.error(uid + " is trying to delete others' notification");
            return Result.error(response, "403", "You can only delete your own notification");
        }
        notificationService.deleteNotification(notificationId);
        return Result.success(response);
    }

    @PutMapping("/changeStatus")
    @Operation(summary = "修改通知状态", description = "传入notificationId和是否read")
    public Result updateReadStatus(HttpServletResponse response,HttpServletRequest request, @RequestParam int notificationId, @RequestParam Boolean read) {
        String uid = (String) request.getAttribute("loginUserId");
        int userType = (int) request.getAttribute("loginUserType");
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            log.error("Notification not exist");
            return Result.error(response, "401", "Notification not exist");
        }
        if (userType != constant_User.ADMIN && !Objects.equals(uid, notification.getNotifiedUserId())) {
            log.error(uid + " is trying to change others' notification's status");
            return Result.error(response, "403", "You can only change your own notification's status");
        }
        notificationService.updateReadStatus(notificationId, read);
        return Result.success(response);
    }

    @GetMapping("/all/{userId}")
    @Operation(summary = "返回指定用户的所有通知", description = "传入userId")
    public Result getAllNotificationsOfOneUser(HttpServletResponse response,HttpServletRequest request,  @PathVariable String userId) {
        String uid = (String) request.getAttribute("loginUserId");
        int userType = (int) request.getAttribute("loginUserType");
        if (userType != constant_User.ADMIN ) {
            log.error(uid + " is trying to get others' notifications");
            return Result.error(response, "403", "Only admin can get others' notifications");
        }
        return Result.success(response, notificationService.getAllNotificationsOfOneUser(userId));
    }

    @GetMapping("/all")
    @Operation(summary = "返回我的所有通知")
    public Result getAllMyNotifications(HttpServletResponse response, HttpServletRequest request) {
        String userId = (String) request.getAttribute("loginUserId");
        return Result.success(response, notificationService.getAllNotificationsOfOneUser(userId));
    }

    @GetMapping("/page")
    @Operation(summary = "分页返回我的所有通知")
    public Result getMyNotificationsByPage(HttpServletResponse response, HttpServletRequest request, @RequestParam int pageNum, @RequestParam int pageSize) {
        if (pageNum < 0) {
            return Result.error(response, "pageNum不能为负数");
        } else if (pageSize <= 0) {
            return Result.error(response, "pageSize应该为正数");
        } else {
            String userId = (String) request.getAttribute("loginUserId");
            return Result.success(response, notificationService.getNotificationsOfOneUserByPage(userId, pageNum, pageSize));
        }

    }
}
