package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.utils.Result;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private IEventService eventService;

    @GetMapping("/getAuditList/{eventStatus}")
    @Operation(summary = "获取审核列表",
            description = """
            ### 参数 ###
            eventStatus(String): 事件状态列表, 逗号分开, 0表示未审核，1表示审核通过，2表示审核未通过
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
                    "startTime": "2024-03-16T19:18:34",
                    "location": "活动1的地址"
                  }
                ]
              }
            ### 注意事项 ###
            无
            """)
    public Result getAuditList(@NotNull HttpServletRequest request,HttpServletResponse response,@PathVariable("eventStatus")String eventStatus) {
        int userType = (int) request.getAttribute("loginUserType");
        if (userType != 0) {
            throw new ServiceException("403","Only admin can alter");
        }
        return Result.success(response,eventService.getAuditList(eventStatus));
    }


    @PostMapping("/changeAudit")
    @Operation(summary = "修改审核状态",
            description = """
            ### 参数 ###
            eventId(Integer): 事件ID
            status(Integer): 审核状态，0表示未审核，1表示审核通过，2表示审核未通过
            ### 返回值 ###
            {
                "code": "200",
                "msg": "Request Success"
            }
            ### 注意事项 ###
            无
            """)
    public Result changeAudit(@NotNull HttpServletRequest request,HttpServletResponse response,@RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        if (userType != 0) {
            throw new ServiceException("403","Only admin can alter");
        }
        Integer eventId = requestBody.getInteger("eventId");
        Integer status = requestBody.getInteger("status");
        String reason = requestBody.getString("reason");
        eventService.changeAudit(eventId,status,reason);
        return Result.success(response);
    }
}
