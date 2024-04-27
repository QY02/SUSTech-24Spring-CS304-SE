package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.*;
import org.cs304.backend.mapper.*;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EventMapper eventMapper;
    @Autowired
    private UserBlogInteractionMapper userBlogInteractionMapper;
    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public List<JSONObject> getAllMoment(Integer momentId, Integer viewType,String userId) {
        List<Comment> commentList;
        if (momentId == -1) {
            if (viewType == 1)
                commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("type",BLOG).orderByDesc("publish_date").last("limit 20"));
            else
                commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("publisher_id",userId).eq("type",BLOG).orderByDesc("publish_date").last("limit 20"));
        }else {
            Comment comment = baseMapper.selectById(momentId);
            if (viewType == 1)
                commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("type",BLOG).orderByDesc("publish_date").gt("publish_date",comment.getPublishDate()).last("limit 20"));
            else
                commentList = baseMapper.selectList(new QueryWrapper<Comment>().select("id,publisher_id").eq("publisher_id",userId).eq("type",BLOG).orderByDesc("publish_date").gt("publish_date",comment.getPublishDate()).last("limit 20"));
        }
        if (commentList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> ids = commentList.stream().map(Comment::getId).toList();
        List<Integer> attachmentIds = new ArrayList<>();
        for (Integer id : ids) {
            attachmentIds.add(entityAttachmentRelationMapper.selectList(new QueryWrapper<EntityAttachmentRelation>().eq("entity_id",id).eq("entity_type",COMMENT)).get(0).getAttachmentId());
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
        List<Integer> attachmentIds = entityAttachmentRelationMapper.selectList(new QueryWrapper<EntityAttachmentRelation>().eq("entity_id",commentId).eq("entity_type",COMMENT)).stream().map(EntityAttachmentRelation::getAttachmentId).toList();
        List<String> attachmentPaths = attachmentService.getBatchByIds(ADMIN,attachmentIds).stream().map(Attachment::getFilePath).toList();
        jsonObject.put("mediaUrl",attachmentPaths);
        return jsonObject;
    }

    @Override
    public void deleteMoment(Integer momentId) {
        List<Integer> attachmentIds = entityAttachmentRelationMapper.selectList(new QueryWrapper<EntityAttachmentRelation>().eq("entity_id",momentId).eq("entity_type",COMMENT)).stream().map(EntityAttachmentRelation::getAttachmentId).toList();
        attachmentService.deleteBatchByIdList(ADMIN,attachmentIds);
        userBlogInteractionMapper.delete(new QueryWrapper<UserBlogInteraction>().eq("blog_id",momentId));
        replyMapper.delete(new QueryWrapper<Reply>().eq("comment_id",momentId));
        entityAttachmentRelationMapper.delete(new QueryWrapper<EntityAttachmentRelation>().eq("entity_id",momentId).eq("entity_type",COMMENT));
        baseMapper.deleteById(momentId);
    }

    @Override
    public JSONObject createMomentStart(JSONObject comment, String userId) {
        Comment comment1 = new Comment();
        comment1.setContent(comment.getString("content"));
        comment1.setPublisherId(userId);
        comment1.setEventId(comment.getInteger("eventId"));
        comment1.setType(BLOG);
        comment1.setPublishDate(LocalDateTime.now());
        comment1.setTitle(comment.getString("title"));
        comment1.setUpVote(0);
        comment1.setDownVote(0);
        Integer mediaType = comment.getInteger("type");
        comment1.setMediaType(mediaType != 1);
        JSONObject requestData = JSONObject.from(comment1);
        requestData.put("serviceClassName", this.getClass().getName());
        requestData.put("serviceMethodName", "createMomentFinish");
        List<String> fileList = comment.getJSONArray("files").toJavaList(String.class);
        List<String> fileDirList = new ArrayList<>();
        for (String ignored : fileList) {
            fileDirList.add("blog/" + "uuid");
        }
        return attachmentService.uploadBatchStart(ADMIN,fileDirList,fileList, requestData);
    }

    @Override
    public JSONObject createMomentFinish(JSONObject comment) {
        Comment comment1 = comment.toJavaObject(Comment.class);
        baseMapper.insert(comment1);
        Integer commentId = comment1.getId();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("commentId",commentId);
        return jsonObject;
    }
}
