package org.eventCenter.fileServer.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eventCenter.fileServer.service.IFileService;
import org.eventCenter.fileServer.utils.Result;
import org.springframework.http.ResponseEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private IFileService fileService;

    @GetMapping("/download")
    public ResponseEntity<?> download(@NotNull HttpServletRequest request) {
        return fileService.download((String) request.getAttribute("filePath"));
    }

    @PostMapping("/upload")
    public Result upload(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        return Result.success(response, fileService.upload((String) request.getAttribute("filePath"), file));
    }

    @PostMapping("/delete")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"filePath\": \"test.txt\"}")))
    public Result delete(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @RequestBody JSONObject requestBody) {
        if (request.getAttribute("admin") == null) {
            return Result.error(response, "401", "This API can only be used with admin token");
        }
        fileService.delete(requestBody.getString("filePath"));
        return Result.success(response);
    }
}
