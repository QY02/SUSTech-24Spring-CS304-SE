package org.cs304.backend;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.entity.*;
import org.cs304.backend.mapper.*;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.cs304.backend.constant.constant_AttachmentType.IMAGE;
import static org.cs304.backend.constant.constant_EntityType.COMMENT;
import static org.cs304.backend.constant.constant_User.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ReplyMapper replyMapper;
    @Mock
    private EventMapper eventMapper;

    @Mock
    private IAttachmentService attachmentService;

    @Mock
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;

    @Mock
    private UserBlogInteractionMapper userBlogInteractionMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(commentService, "baseMapper", commentMapper);

    }

    @Test
    @DisplayName("Should get all moments")
    public void shouldGetAllMoment() {
        Integer momentId = -1;
        Integer viewType = 1;
        String userId = "1";

        Comment comment = new Comment();
        comment.setId(1);
        comment.setPublisherId(userId);

        EntityAttachmentRelation entityAttachmentRelation = new EntityAttachmentRelation();
        entityAttachmentRelation.setAttachmentId(1);
        entityAttachmentRelation.setEntityId(1);
        entityAttachmentRelation.setEntityType(COMMENT);
        entityAttachmentRelation.setAttachmentType(IMAGE);


        when(entityAttachmentRelationMapper.selectList(any())).thenReturn(List.of(entityAttachmentRelation));
        when(commentMapper.selectList(any())).thenReturn(List.of(comment));

        List<JSONObject> result = commentService.getAllMoment(momentId, viewType, userId);

    }

    @Test
    @DisplayName("Should get moment by id")
    public void shouldGetMomentById() {
        Integer commentId = 1;

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPublisherId("1");
        comment.setEventId(1);
        comment.setMediaType(Boolean.TRUE);

        User user = new User();
        user.setName("Test User");

        Event event = new Event();
        event.setName("Test Event");

        when(commentMapper.selectById(any())).thenReturn(comment);
        when(userMapper.selectById(any())).thenReturn(user);
        when(eventMapper.selectById(any())).thenReturn(event);

        JSONObject result = commentService.getMomentById(commentId);

        assertEquals(commentId, result.getInteger("id"));
        assertEquals(user.getName(), result.getString("userName"));
        assertEquals(event.getName(), result.getString("relatedEvent"));
    }

    @Test
    @DisplayName("Should delete moment by id")
    public void shouldDeleteMoment() {
        Integer momentId = 1;
        when(commentMapper.deleteById(1)).thenReturn(1);
        when(entityAttachmentRelationMapper.selectList(any())).thenReturn(List.of(new EntityAttachmentRelation()));
        when(userBlogInteractionMapper.delete(any())).thenReturn(1);
        when(replyMapper.delete(any())).thenReturn(1);
        when(entityAttachmentRelationMapper.delete(any())).thenReturn(1);
        assertDoesNotThrow(() -> commentService.deleteMoment(momentId));
    }

    @Test
    @DisplayName("Should start creating a moment")
    public void shouldCreateMomentStart() {
        String userId = "1";
        JSONObject comment = new JSONObject();
        comment.put("content", "Test content");
        comment.put("eventId", 1);
        comment.put("title", "Test title");
        comment.put("type", 1);
        comment.put("files", new JSONArray());
        JSONObject result = commentService.createMomentStart(comment, userId);

    }

    @Test
    @DisplayName("Should finish creating a moment")
    public void shouldCreateMomentFinish() {
        Comment comment = new Comment();
        comment.setContent("Test content");
        comment.setPublisherId("1");
        comment.setEventId(1);
        comment.setType(COMMENT);
        comment.setPublishDate(null);
        comment.setTitle("Test title");
        comment.setUpVote(0);
        comment.setDownVote(0);
        comment.setMediaType(Boolean.TRUE);

        JSONObject requestData = JSONObject.parseObject(JSONObject.toJSONString(comment));
        requestData.put("serviceClassName", this.getClass().getName());
        requestData.put("serviceMethodName", "createMomentFinish");

        List<String> fileList = new ArrayList<>();
        fileList.add("Test file");

        requestData.put("files", fileList);

        List<String> fileDirList = new ArrayList<>();
        requestData.put("fileInfoList", fileDirList);

        when(commentMapper.insert(any())).thenReturn(1);

        commentService.createMomentFinish(requestData);

    }
}