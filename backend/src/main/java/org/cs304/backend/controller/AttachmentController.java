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

import java.util.List;

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

    @PostMapping("/getBatchByIds")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"idList\": [1, 2, 3]}")))
    public Result getBatchByIds(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        List<Integer> idList = requestBody.getList("idList", Integer.class);
        return Result.success(response, attachmentService.getBatchByIds(userType, idList));
    }

    @PostMapping("/uploadStart")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"fileDir\": \"test\"}")))
    public Result uploadStart(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String fileDir = requestBody.getString("fileDir");
        return Result.success(response, attachmentService.uploadStart(userType, fileDir, null));
    }

    @PostMapping("/uploadFinish")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"filePath\": \"test.txt\", \"requestData\": {}}")))
    public Result uploadFinish(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        String filePath = requestBody.getString("filePath");
        JSONObject requestData = requestBody.getJSONObject("requestData");
        return Result.success(response, attachmentService.uploadFinish(userType, filePath, requestData));
    }

    @PostMapping("/uploadBatchStart")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "fileDirList": ["test", "test1", "test2"],
              "fileNameList": ["file.txt", "file1.txt", "file2.txt"]
            }""")))
    public Result uploadBatchStart(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        List<String> fileDirList = requestBody.getList("fileDirList", String.class);
        List<String> fileNameList = requestBody.getList("fileNameList", String.class);
        return Result.success(response, attachmentService.uploadBatchStart(userType, fileDirList, fileNameList, null));
    }

    @PostMapping("/uploadBatchFinish")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"filePathList\": [\"test/file.txt\", \"test1/file1.txt\", \"test2/file2.txt\"], \"requestData\": {}}")))
    public Result uploadBatchFinish(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        List<String> filePathList = requestBody.getList("filePathList", String.class);
        JSONObject requestData = requestBody.getJSONObject("requestData");
        return Result.success(response, attachmentService.uploadBatchFinish(userType, filePathList, requestData));
    }

    @PostMapping("/deleteById")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"id\": 1}")))
    public Result deleteById(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer id = requestBody.getInteger("id");
        attachmentService.deleteById(userType, id);
        return Result.success(response);
    }

    @PostMapping("/deleteBatchByIdList")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"idList\": [1, 2, 3]}")))
    public Result deleteBatchByIdList(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        List<Integer> idList = requestBody.getList("idList", Integer.class);
        attachmentService.deleteBatchByIdList(userType, idList);
        return Result.success(response);
    }
}
