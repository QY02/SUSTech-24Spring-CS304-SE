package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.controller.UserBlogInteractionController;
import org.cs304.backend.service.IUserBlogInteractionService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserBlogInteractionControllerTest {

    @InjectMocks
    UserBlogInteractionController userBlogInteractionController;

    @Mock
    IUserBlogInteractionService userBlogInteractionService;

    MockHttpServletResponse response;

    MockHttpServletRequest request;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
    }

    @Test
    @DisplayName("Should return a blog when getting a blog by comment id")
    public void shouldReturnBlogWhenGettingBlogByCommentId() {
        JSONObject blog = new JSONObject();
        blog.put("commentId", 1);

        when(userBlogInteractionService.getBlog(1,"1")).thenReturn(blog);

        Result result = userBlogInteractionController.getBlog(request,response, 1);

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return success when changing a vote")
    public void shouldReturnSuccessWhenChangingVote() {

        Result result = userBlogInteractionController.changeVote(response, 1, "0", 1);

        assertEquals("200", result.getCode());
    }
}