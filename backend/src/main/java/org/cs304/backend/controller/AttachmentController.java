package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Resource
    private IAttachmentService attachmentService;

    @PostMapping("/getById")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"id\": 1}")))
    public Result getById(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer id = requestBody.getInteger("id");
        return Result.success(response, attachmentService.getById(userType, id));
    }

    @PostMapping("/uploadStart")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"fileDir\": \"test\"}")))
    public Result uploadStart(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String fileDir = requestBody.getString("fileDir");
        return Result.success(response, attachmentService.uploadStart(userType, fileDir));
    }

    @PostMapping("/uploadFinish")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"filePath\": \"test.txt\"}")))
    public Result uploadFinish(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String filePath = requestBody.getString("filePath");
        return Result.success(response, attachmentService.uploadFinish(userType, filePath));
    }

    @PostMapping("/deleteById")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"id\": 1}")))
    public Result deleteById(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer id = requestBody.getInteger("id");
        attachmentService.deleteById(userType, id);
        return Result.success(response);
    }
}
