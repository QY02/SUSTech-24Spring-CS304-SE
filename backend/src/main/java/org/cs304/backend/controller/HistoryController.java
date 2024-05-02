package org.cs304.backend.controller;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.service.IHistoryService;
import org.cs304.backend.service.IUserInteractionService;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/history")
public class HistoryController {
    @Resource
    private IHistoryService historyService;

    @Resource
    private IUserInteractionService userInteractionService;
    @PostMapping("/add")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "eventId": 0,
              "userId": "12110141"
            }""")))
    public Result addEventHistory(HttpServletResponse response,  @RequestBody JSONObject requestBody) {
        String userId = requestBody.getString("userId");
        Integer eventId = requestBody.getInteger("eventId");
//        LocalDateTime visitTime=requestBody.getObject("visit_time");
        historyService.addEventHistory(userId,eventId);
        userInteractionService.changeUserInteraction(userId,eventId,1,2);
        return Result.success(response);
    }
    @PostMapping("/getByUserId")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "userId": "12110141"
            }""")))
    public Result getAllHistory(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String userId = requestBody.getString("userId");
        return Result.success(response, historyService.getAllHistory(userId));
    }
}
