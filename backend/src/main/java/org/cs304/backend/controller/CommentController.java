package org.cs304.backend.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.service.ICommentService;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private ICommentService commentService;
    @PostMapping("/add")
    public Result postNewComment(HttpServletResponse response, @RequestParam Comment comment) {
        commentService.save(comment);
        return Result.success(response);
    }




}
