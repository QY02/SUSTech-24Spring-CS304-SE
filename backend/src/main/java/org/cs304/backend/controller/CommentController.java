package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.service.ICommentService;
import org.cs304.backend.service.INotificationService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ICommentService commentService;

    @Resource
    private INotificationService notificationService;

    @PostMapping("/add")
    @Operation(summary = "添加评论",description = "传入comment结构内容")
    public Result postNewComment(HttpServletResponse response, @RequestBody Comment comment) {
        commentMapper.insert(comment);
        return Result.success(response);
    }

    @PostMapping("/createMoment")
    @Operation(summary = "创建动态",description = "传入comment结构内容，外加fileList，为图片的名称列表")
    public Result createMoment(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject comment) {
        String userId = request.getAttribute("loginUserId").toString();
        JSONObject fileUrl = commentService.createMomentStart(comment, userId);
        return Result.success(response,fileUrl);
    }


    @PostMapping("/getByEvent")
    @Operation(summary = "获取一个事件下的评论或动态",description = "传入eventId和type，type为0时获取评论，为1时获取动态")
    public Result getEventComment(HttpServletResponse response, @RequestBody JSONObject jsonObject) {
        Integer eventId = jsonObject.getInteger("eventId");
        if (eventId == null) {
            throw new ServiceException("eventId不能为空");
        }
        Integer type = jsonObject.getInteger("type");
        if (type == null) {
            throw new ServiceException("type不能为空");
        }
        return Result.success(response,commentMapper.selectList(new QueryWrapper<Comment>().eq("event_id",eventId).eq("type",type)));
    }

    @GetMapping("/getById")
    @Operation(summary = "获取一个评论",description = "传入commentId")
    public Result getCommentById(HttpServletResponse response, @RequestParam Integer commentId) {
        return Result.success(response,commentMapper.selectById(commentId));
    }

    @GetMapping("/getMomentById")
    @Operation(summary = "获取一个动态",description = "传入commentId")
    public Result getMomentById(HttpServletResponse response, @RequestParam Integer commentId) {
        JSONObject moment = commentService.getMomentById(commentId);
        return Result.success(response,moment);
    }

    @GetMapping("/delete/{commentId}")
    @Operation(summary = "删除一个评论",description = "传入path的eventId，不判断是否有delete按钮 所以要在前端控制")
    public Result deleteComment(HttpServletResponse response, @PathVariable("commentId")Integer commentId) {
        return Result.success(response,commentMapper.deleteById(commentId));
    }

    @GetMapping("/getMomentBatch/{momentId}/{viewType}")
    @Operation(summary = "获取所有动态",description = "传入path的momentId，为之前传递的最后一个momentId，返回20个；第一次传-1。viewType为1时获取所有动态，为2时获取我的动态")
    public Result getAllMoment(HttpServletRequest request,HttpServletResponse response, @PathVariable("momentId")Integer momentId, @PathVariable("viewType")Integer viewType) {
        String userId = request.getAttribute("loginUserId").toString();
        List<JSONObject> momentList = commentService.getAllMoment(momentId, viewType, userId);
        return Result.success(response,momentList);
    }

    @DeleteMapping("/deleteMoment/{momentId}")
    @Operation(summary = "删除一个动态",description = "传入path的momentId")
    public Result deleteMoment(HttpServletResponse response, @PathVariable("momentId")Integer momentId) {
        commentService.deleteMoment(momentId);
        return Result.success(response);
    }

    @PostMapping("/deleteMomentByAdmin")
    @Operation(summary = "管理员删除一个动态",description = "传入momentId")
    public Result deleteReplyByAdmin(HttpServletRequest request,HttpServletResponse response, @RequestBody JSONObject jsonObject) {
        Integer momentId = jsonObject.getInteger("momentId");
        String deleteReason = jsonObject.getString("deleteReason");
        int userType = (int) request.getAttribute("loginUserType");
        if (userType != constant_User.ADMIN) {
            return Result.error(response, "403", "Only admin can alter");
        }
        Comment moment = commentMapper.selectById(momentId);
        String adminId = request.getAttribute("loginUserId").toString();
        String title = "您的动态被管理员删除";
        String content = "您的动态 \""+moment.getTitle()+"\" 被管理员删除，原因：" + deleteReason;
        notificationService.insertAdminNotification(adminId, moment.getPublisherId(), title, content);
        commentService.deleteMoment(momentId);
        return Result.success(response);
    }
}
