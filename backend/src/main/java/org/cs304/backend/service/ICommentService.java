package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ICommentService extends IService<Comment> {

    List<JSONObject> getAllMoment(Integer momentId);

    JSONObject getMomentById(Integer commentId);
}
