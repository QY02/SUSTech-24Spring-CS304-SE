package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.service.IOrderRecordService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderRecord")
public class OrderRecordController {
    @Resource
    private IOrderRecordService orderRecordService;

    @PostMapping("/getMyOrderRecordByEventId")
    @Operation(description = """
            ### 参数 ###
            mode = 0: 只返回eventSessionId,
            
            mode = 1: 返回所有数据
            """)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"eventId\": 1, \"mode\": 0}")))
    public Result getMyOrderRecordByEventId(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        String userId = (String) request.getAttribute("loginUserId");
        Integer eventId = requestBody.getInteger("eventId");
        Integer mode = requestBody.getInteger("mode");
        return Result.success(response, orderRecordService.getMyOrderRecordByEventId(userId, eventId, mode));
    }


}
