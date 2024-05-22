package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.service.ISeatMapService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seatMap")
public class SeatMapController {
    @Resource
    private ISeatMapService seatMapService;
    @PostMapping("/getSeatMapWithSeatsById")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"seatMapId\": 1}")))
    public Result getSeatMapWithSeatsById(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer seatMapId = requestBody.getInteger("seatMapId");
        return Result.success(response, seatMapService.getSeatMapWithSeatsById(userType, seatMapId));
    }
    @GetMapping("/getAllSeatMapTemplate")
    public Result getAllSeatMapTemplate(HttpServletResponse response) {
        return Result.success(response, seatMapService.getAllSeatMapTemplate());
    }
}
