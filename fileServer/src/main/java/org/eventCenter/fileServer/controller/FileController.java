package org.eventCenter.fileServer.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.eventCenter.fileServer.service.IFileService;
import org.springframework.http.ResponseEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    private IFileService fileService;

    @GetMapping("/download")
    public ResponseEntity<?> download(@NotNull HttpServletRequest request) {
        return fileService.download((String) request.getAttribute("filePath"));
    }
}
