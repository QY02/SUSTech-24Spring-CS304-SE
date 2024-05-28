package org.cs304.backend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.controller.ChatMessageController;
import org.cs304.backend.entity.ChatMessage;
import org.cs304.backend.entity.User;
import org.cs304.backend.mapper.ChatMessageMapper;
import org.cs304.backend.mapper.UserMapper;
import org.cs304.backend.service.IUserService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

public class ChatMessageControllerTest {

    @InjectMocks
    ChatMessageController chatMessageController;

    @Mock
    ChatMessageMapper chatMessageMapper;

    @Mock
    IUserService userService;

    @Mock
    UserMapper userMapper;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should return chat messages when valid users are provided")
    public void shouldReturnChatMessagesWhenValidUsersAreProvided() {
        request.setAttribute("loginUserId", "1");
        User user = new User();
        user.setName("user1");
        user.setIconId(1);
        when(userMapper.selectById("2")).thenReturn(user);
        when(userMapper.selectById("1")).thenReturn(user);

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage());
        when(chatMessageMapper.selectList(new QueryWrapper<ChatMessage>().eq("receiver_id", "1").eq("sender_id", "2").or().eq("receiver_id", "2").eq("sender_id", "1").orderByDesc("send_time").last("LIMIT 50"))).thenReturn(chatMessages);
        when(chatMessageMapper.update(new ChatMessage(), new QueryWrapper<ChatMessage>().eq("receiver_id", "1").eq("sender_id", "2").eq("is_read", 0))).thenReturn(1);
        Result result = chatMessageController.onLogin(request, response, "2");

        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return error when exception is thrown")
    public void shouldReturnErrorWhenExceptionIsThrown() {
        request.setAttribute("loginUserId", "1");
        when(userService.getById("1")).thenThrow(new RuntimeException());

        Result result = chatMessageController.onLogin(request, response, "2");

        assertEquals("500", result.getCode());
        assertEquals("fail to get chat message", result.getMsg());
    }

    @Test
    @DisplayName("Should return unread messages when valid user is provided")
    public void shouldReturnUnreadMessagesWhenValidUserIsProvided() {
        request.setAttribute("loginUserId", "1");
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSenderId("2");
        chatMessages.add(chatMessage);
        when(chatMessageMapper.selectList(any(QueryWrapper.class))).thenReturn(chatMessages);

        User user = new User();
        user.setId("2");
        user.setName("user2");
        user.setIconId(2);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userMapper.selectBatchIds(anyCollection())).thenReturn(users);
        Result result = chatMessageController.getUnread(request, response);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Should return unread messages when no valid user is provided")
    public void shouldReturnUnreadMessagesWhenNoValidUserIsProvided() {
        request.setAttribute("loginUserId", "1");
        List<ChatMessage> chatMessages = new ArrayList<>();
        when(chatMessageMapper.selectList(any(QueryWrapper.class))).thenReturn(chatMessages);

        List<User> users = new ArrayList<>();
        when(userMapper.selectBatchIds(anyCollection())).thenReturn(users);
        Result result = chatMessageController.getUnread(request, response);
        assertEquals("200", result.getCode());
    }
}
