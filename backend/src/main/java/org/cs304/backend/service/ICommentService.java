package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICommentService extends IService<Comment> {

    List<JSONObject> getAllMoment(Integer momentId, Integer viewType,String userId);

    JSONObject getMomentById(Integer commentId);

    @Transactional(rollbackFor = {Exception.class})
    void deleteMoment(Integer momentId);

    @Transactional(rollbackFor = {Exception.class})
    JSONObject createMoment(JSONObject comment, String userId);
}
