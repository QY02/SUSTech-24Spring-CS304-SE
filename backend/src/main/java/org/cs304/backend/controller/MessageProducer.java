package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public Result sendMessage(@NotNull HttpServletResponse response) {
        String session_ID = "123456";
        String query = "你好，我想了解一下南方科技大学的音乐会信息。";
        String LLM_type = "TEST";
        List history = new ArrayList();
        String api_key = "api-key";
        String event = "query";
        JSONObject msgData = new JSONObject();
        msgData.put("session_ID", session_ID);
        msgData.put("history", history);
        msgData.put("query", query);
        msgData.put("LLM_type", LLM_type);
        msgData.put("api_key", api_key);
        msgData.put("event", event);
        System.out.println(msgData);
        rabbitTemplate.convertAndSend("ChatProduceExchange", "ChatProduceRouting", msgData.toString());
        return Result.success(response);
    }
}