package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.entity.EntityAttachmentRelation;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.mapper.EventMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.ICommentService;
import org.cs304.backend.service.IEntityAttachmentRelationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cs304.backend.constant.constant_CommentType.BLOG;
import static org.cs304.backend.constant.constant_EntityType.COMMENT;
import static org.cs304.backend.constant.constant_User.ADMIN;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private IAttachmentService attachmentService;

    @Resource
    private IEntityAttachmentRelationService entityAttachmentRelationService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EventMapper eventMapper;

    @Override
    public List<JSONObject> getAllMoment(Integer momentId) {
        List<Comment> commentList;
        if (momentId == -1) {
            commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("type",BLOG).orderByDesc("publish_date").last("limit 20"));
        }else {
            Comment comment = baseMapper.selectById(momentId);
            commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("type",BLOG).orderByDesc("publish_date").lt("publish_date",comment.getPublishDate()).last("limit 20"));
        }
        if (commentList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> ids = commentList.stream().map(Comment::getId).toList();
        List<Integer> attachmentIds = new ArrayList<>();
        for (Integer id : ids) {
            attachmentIds.add(entityAttachmentRelationService.getOne(new QueryWrapper<EntityAttachmentRelation>().eq("entity_id",id).eq("entity_type",COMMENT)).getAttachmentId());
        }
        Map<Integer, Comment> attachmentIdToCommentMap = new HashMap<>();
        for (int i = 0; i < attachmentIds.size(); i++) {
            attachmentIdToCommentMap.put(attachmentIds.get(i), commentList.get(i));
        }
        return attachmentService.getBatchByIds(ADMIN,attachmentIds).stream().map(
                attachment -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("attachment",attachment.getFilePath());
                    jsonObject.put("comment_id",attachmentIdToCommentMap.get(attachment.getId()).getId());
                    jsonObject.put("publisher_id",attachmentIdToCommentMap.get(attachment.getId()).getPublisherId());
                    return jsonObject;
                }).toList();
    }

    @Override
    public JSONObject getMomentById(Integer commentId) {
        Comment comment = baseMapper.selectById(commentId);
        String username = userMapper.selectById(comment.getPublisherId()).getName();
        String eventName = eventMapper.selectById(comment.getEventId()).getName();
        JSONObject jsonObject = (JSONObject) JSON.toJSON(comment);
        jsonObject.put("userName",username);
        jsonObject.put("relatedEvent",eventName);
        return jsonObject;
    }
}
