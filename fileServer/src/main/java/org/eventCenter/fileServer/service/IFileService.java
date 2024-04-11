package org.eventCenter.fileServer.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    ResponseEntity<?> download(String filePath);

    void delete(String filePath);

    JSONObject upload(String fileDir, MultipartFile inputFile);
}
