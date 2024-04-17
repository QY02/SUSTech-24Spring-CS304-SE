package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.entity.Event;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/event")
public class EventController {

    @Resource
    private IEventService eventService;

    @PostMapping("/add")
    public Result postNewEvent(HttpServletResponse response, @RequestParam Event event) {
        eventService.save(event);
        return Result.success(response);
    }

    @PostMapping("/getEventSessionsByEventId")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"eventId\": 1}")))
    public Result getEventSessionsByEventId(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer eventId = requestBody.getInteger("eventId");
        return Result.success(response, eventService.getEventSessionsByEventId(userType, eventId));
    }

    @PostMapping("/submitBookingData")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "eventId": 0,
              "eventSessionId": 0,
              "seatId": "string",
              "additionalInformation": "string"
            }""")))
    public Result submitBookingData(HttpServletResponse response, HttpServletRequest request, @RequestBody OrderRecord orderRecord) {
        int userType = (int) request.getAttribute("loginUserType");
        String userId = (String) request.getAttribute("loginUserId");
        eventService.submitBookingData(userType, userId, orderRecord);
        return Result.success(response);
    }

    @PostMapping("/getAllEvents")
    @Operation(summary = "获取所有events", description = """
            ### 参数 ###
            无
            ### 返回值 ###
            {
                "code": "200",
                "msg": "Request Success",
                "data": [
                  {
                    "content": "活动1的内容",
                    "id": 1,
                    "lowestPrice": 0,
                    "name": "活动1",
                    "publishDate": "2024-03-15T19:08:08",
                    "publisherId": "12112003",
                    "status": 0,
                    "type": 0,
                  }
                ]
              }
            ### 注意事项 ###
            无
            """)
    public Result getAllEvents(@NotNull HttpServletRequest request, HttpServletResponse response) {
        int userType = (int) request.getAttribute("loginUserType");
        System.out.println(userType+"666666666666666666");
//        if (userType == -1) {
//            throw new ServiceException("403", "Permission denied");
//        }
        return Result.success(response, eventService.getAllEvents());
    }


    @GetMapping("/getEventDetail/{eventId}")
    public Result getEventDetail(HttpServletResponse response, @PathVariable("eventId")int eventId) {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", eventId);
        if (!eventService.exists(queryWrapper)) {
            throw new ServiceException("404","No such event");
        }
        return Result.success(response,eventService.getById(eventId));
    }
}
