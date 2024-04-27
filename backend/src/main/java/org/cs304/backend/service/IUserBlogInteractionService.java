package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cs304.backend.entity.UserBlogInteraction;

public interface IUserBlogInteractionService extends IService<UserBlogInteraction> {
    JSONObject getBlog(Integer commentId);
    void changeVote(Integer commentId, String userId, Integer voteType);
}
