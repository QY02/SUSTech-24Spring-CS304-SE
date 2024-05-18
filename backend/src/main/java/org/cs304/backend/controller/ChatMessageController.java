package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.ChatMessage;
import org.cs304.backend.service.IChatMessageService;
import org.cs304.backend.service.IUserService;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chatMessage")
public class ChatMessageController {

    @Resource
    IChatMessageService chatMessageService;

    @Resource
    IUserService userService;

    /**
     * This method is used to fetch chat messages between two users.
     * It is mapped to the "/get/{toUserID}" endpoint and is invoked on HTTP POST requests.
     *
     * @param request  The HttpServletRequest object that contains the request the client made of the servlet.
     * @param response The HttpServletResponse object that contains the response the servlet sends to the client.
     * @param toUserID The ID of the user to whom the chat messages are to be fetched.
     *
     * @return Result object that contains the response code, message and a list of chat messages.
     *
     */
    @PostMapping("/get/{toUserID}")
    @Operation(summary = "获取两个用户之间的聊天记录", description = "传入toUserID")
    public Result onLogin(@NotNull HttpServletRequest request, HttpServletResponse response, @PathVariable ("toUserID")String toUserID) {
        try {
            String userID = (String) request.getAttribute("loginUserId");
            String userName = userService.getById(userID).getName();
            String toUserName = userService.getById(toUserID).getName();
            HashMap<String, String> map = new HashMap<>();
            map.put(userID, userName);
            map.put(toUserID, toUserName);
            List<ChatMessage> allMessages = chatMessageService.list(new QueryWrapper<ChatMessage>().eq("receiver_id", userID).eq("sender_id", toUserID).or().eq("receiver_id", toUserID).eq("sender_id", userID).orderByDesc("send_time").last("LIMIT 50"));
            allMessages.sort(Comparator.comparing(ChatMessage::getSendTime));
            List<JSONObject> allMessages0 = allMessages.stream().map(s -> {
                JSONObject json = (JSONObject) JSON.toJSON(s);
                json.put("receiverName", map.get(s.getReceiverId()));
                json.put("senderName", map.get(s.getSenderId()));
                return json;
            }).toList();
            return Result.success(response, allMessages0);
        }catch (Exception e) {
            log.error(e.getMessage());
            return Result.error(response, "fail to get chat message");
        }
    }
}