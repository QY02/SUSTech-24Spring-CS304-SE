package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.CommentController;
import org.cs304.backend.mapper.CommentMapper;
import org.cs304.backend.service.ICommentService;
import org.cs304.backend.entity.Comment;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CommentControllerTest {

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentMapper commentMapper;

    @Mock
    ICommentService commentService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return success when posting a new comment")
    public void shouldReturnSuccessWhenPostingNewComment() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Test content");

        when(commentMapper.insert(comment)).thenReturn(1);

        Result result = commentController.postNewComment(response, comment);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return a comment when getting a comment by id")
    public void shouldReturnCommentWhenGettingCommentById() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setContent("Test content");

        when(commentService.getMomentById(1)).thenReturn(JSONObject.from(comment));

        Result result = commentController.getCommentById(response, 1);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when deleting a comment")
    public void shouldReturnSuccessWhenDeletingComment() {


        Result result = commentController.deleteComment(response, 1);

        assertEquals("200", result.getCode());
    }

    @Test
@DisplayName("Should return success when creating a moment")
public void shouldReturnSuccessWhenCreatingMoment() {
        request.setAttribute("loginUserId", "0");
    JSONObject comment = new JSONObject();
    comment.put("id", 1);
    comment.put("content", "Test content");

    when(commentService.createMomentStart(comment, "0")).thenReturn(new JSONObject());

    Result result = commentController.createMoment(request, response, comment);

    assertEquals("200", result.getCode());
}

@Test
@DisplayName("Should return a list of comments when getting event comments")
public void shouldReturnEventComments() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("eventId", 1);
    jsonObject.put("type", 0);

    when(commentMapper.selectList(new QueryWrapper<Comment>().eq("event_id",1).eq("type",0))).thenReturn(new ArrayList<>());

    Result result = commentController.getEventComment(response, jsonObject);

    assertEquals("200", result.getCode());
}

@Test
@DisplayName("Should return a moment when getting a moment by id")
public void shouldReturnMomentWhenGettingMomentById() {
    JSONObject moment = new JSONObject();
    moment.put("id", 1);
    moment.put("content", "Test content");

    when(commentService.getMomentById(1)).thenReturn(moment);

    Result result = commentController.getMomentById(response, 1);

    assertEquals("200", result.getCode());
}

@Test
@DisplayName("Should return success when deleting a moment")
public void shouldReturnSuccessWhenDeletingMoment() {

    Result result = commentController.deleteMoment(response, 1);

    assertEquals("200", result.getCode());
}

@Test
@DisplayName("Should return a list of moments when getting all moments")
public void shouldReturnAllMoments() {
        request.setAttribute("loginUserId", "0");
    when(commentService.getAllMoment(-1, 1, "0")).thenReturn(new ArrayList<>());

    Result result = commentController.getAllMoment(request, response, -1, 1);

    assertEquals("200", result.getCode());
}

@Test
@DisplayName("Should return success when an admin deletes a moment")
public void shouldReturnSuccessWhenAdminDeletesMoment() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("momentId", 1);
    jsonObject.put("deleteReason", "Test reason");

    request.setAttribute("loginUserType", 0);
    request.setAttribute("loginUserId", "0");

    when(commentMapper.selectById(1)).thenReturn(new Comment());
    when(commentMapper.deleteById(1)).thenReturn(1);

    Result result = commentController.deleteReplyByAdmin(request, response, jsonObject);

    assertEquals("200", result.getCode());
}
}