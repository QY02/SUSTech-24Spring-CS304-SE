package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.service.IUserBlogInteractionService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/blog")
public class UserBlogInteractionController {

    @Resource
    private IUserBlogInteractionService userBlogInteractionService;

    @GetMapping("/get/{commentId}")
    @Operation(summary = "获取一个动态的点赞情况",description = "传入path的eventId")
    public Result getBlog(HttpServletResponse response, @PathVariable("commentId")Integer commentId) {
        JSONObject userBlogInteraction = userBlogInteractionService.getBlog(commentId);
        return Result.success(response, userBlogInteraction);
    }

    @GetMapping("/change/{commentId}/{userId}/{voteType}")
    @Operation(summary = "改变一个动态的点赞情况",description = "传入path的eventId,userId,voteType")
    public Result changeVote(HttpServletResponse response, @PathVariable("commentId")Integer commentId, @PathVariable("userId")String userId, @PathVariable("voteType")Integer voteType) {
        userBlogInteractionService.changeVote(commentId, userId, voteType);
        return Result.success(response);
    }

}
