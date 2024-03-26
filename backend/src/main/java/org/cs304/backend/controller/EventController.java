package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.entity.Event;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
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
}
