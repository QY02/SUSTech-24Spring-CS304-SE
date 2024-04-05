package org.cs304.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.Event;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.service.ICommentService;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private ICommentService commentService;
    @PostMapping("/add")
    @Operation(summary = "添加评论",description = "传入comment结构内容")
    public Result postNewComment(HttpServletResponse response, @RequestParam Comment comment) {
        commentService.save(comment);
        return Result.success(response);
    }

    @GetMapping("/get/{eventId}")
    @Operation(summary = "获取一个事件下的评论",description = "传入path的eventId")
    public Result getEventComment(HttpServletResponse response, @PathVariable("eventId")int eventId) {
        return Result.success(response,commentService.getById(eventId));
    }

    @GetMapping("/delete/{commentId}")
    @Operation(summary = "删除一个评论",description = "传入path的eventId，不判断是否有delete按钮 所以要在前端控制")
    public Result deleteComment(HttpServletResponse response, @PathVariable("commentId")int commentId) {
        return Result.success(response,commentService.removeById(commentId));
    }




}
