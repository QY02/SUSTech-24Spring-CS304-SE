package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.ReplyController;
import org.cs304.backend.entity.Reply;
import org.cs304.backend.mapper.ReplyMapper;
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

public class ReplyControllerTest {

    @InjectMocks
    ReplyController replyController;

    @Mock
    ReplyMapper replyMapper;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return success when adding a reply")
    public void shouldReturnSuccessWhenAddingReply() {
        Reply reply = new Reply();
        reply.setId(1);
        reply.setContent("Test content");

        when(replyMapper.insert(reply)).thenReturn(1);

        Result result = replyController.addReply(response, reply);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return a list of replies when getting replies by comment id")
    public void shouldReturnRepliesWhenGettingRepliesByCommentId() {
        when(replyMapper.selectList(new QueryWrapper<Reply>().eq("comment_id", 1))).thenReturn(new ArrayList<>());

        Result result = replyController.getReplyByComment(response, 1);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when deleting a reply")
    public void shouldReturnSuccessWhenDeletingReply() {
        when(replyMapper.deleteById(1)).thenReturn(1);

        Result result = replyController.deleteReply(response, 1);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when an admin deletes a reply")
    public void shouldReturnSuccessWhenAdminDeletesReply() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("replyId", 1);
        jsonObject.put("deleteReason", "Test reason");

        request.setAttribute("loginUserType", 0);
        request.setAttribute("loginUserId", "0");

        when(replyMapper.selectById(1)).thenReturn(new Reply());
        when(replyMapper.deleteById(1)).thenReturn(1);

        Result result = replyController.deleteReplyByAdmin(request, response, jsonObject);

        assertEquals("200", result.getCode());
    }
}