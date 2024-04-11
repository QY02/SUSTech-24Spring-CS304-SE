package org.eventCenter.fileServer.service.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eventCenter.fileServer.component.GlobalData;
import org.eventCenter.fileServer.exception.ServiceException;
import org.eventCenter.fileServer.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Value("${backend.host:}")
    private String backendHost;

    @Value("${backend.port:}")
    private String backendPort;

    @Value("${backend.api-token:}")
    private String backendApiToken;

    @Override
    public ResponseEntity<?> download(String filePath) {
        if (filePath == null) {
            throw new ServiceException("400", "This API can not be used with admin token");
        }
        File file = new File(GlobalData.FILE_DIRECTORY, filePath);
        if (!file.isFile()) {
            throw new ServiceException("500", "File not exist");
        }
        try {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getName().substring(33), StandardCharsets.UTF_8)).body(new InputStreamResource(new FileInputStream(file)));
        } catch (IOException e) {
            throw new ServiceException("500", "Download failed");
        }
    }

    @Override
    public void delete(String filePath) {
        if (filePath == null) {
            throw new ServiceException("400", "Invalid data");
        }
        File file = new File(GlobalData.FILE_DIRECTORY, filePath);
        if (!file.isFile()) {
            throw new ServiceException("400", "File not exist");
        }
        try {
            boolean result = file.delete();
            if (!result) {
                throw new ServiceException("500", "Delete failed");
            }
        } catch (SecurityException e) {
            throw new ServiceException("500", "Delete failed");
        }
    }

    @Override
    public JSONObject upload(String fileDir, MultipartFile inputFile) {
        if (backendHost.isBlank() || backendPort.isBlank() || backendApiToken.isBlank()) {
            throw new ServiceException("500", "This API is currently unavailable");
        }
        if (fileDir == null) {
            throw new ServiceException("400", "This API can not be used with admin token");
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "-" + inputFile.getOriginalFilename();
        File file = Paths.get(GlobalData.FILE_DIRECTORY, fileDir, fileName).toFile();
        while (true) {
            if (!file.exists()) {
                break;
            }
            else {
                fileName = UUID.randomUUID().toString().replaceAll("-", "") + "-" + inputFile.getOriginalFilename();
                file = Paths.get(GlobalData.FILE_DIRECTORY, fileDir, fileName).toFile();
            }
        }
        boolean result = file.mkdirs();
        if (!result) {
            throw new ServiceException("500", "Unable to create the directory");
        }
        else {
            try {
                inputFile.transferTo(file);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                JSONObject requestBody = new JSONObject();
                requestBody.put("filePath", Paths.get(fileDir, fileName).toString());
                httpHeaders.set("token", backendApiToken);
                try {
                    HttpEntity<JSONObject> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
                    ResponseEntity<JSONObject> responseFromBackend = restTemplate.exchange("http://" + backendHost + ":" + backendPort + "/attachment/uploadFinish", HttpMethod.POST, httpEntity, JSONObject.class);
                    if (responseFromBackend.getStatusCode().value() != 200) {
                        throw new ServiceException("500", "An error occurred when communicating with the backend");
                    }
                    else {
                        return Objects.requireNonNull(responseFromBackend.getBody()).getJSONObject("data");
                    }
                } catch (RestClientException e) {
                    throw new ServiceException("500", "An error occurred when communicating with the backend");
                }
            } catch (Exception e) {
                try {
                    boolean ignore = file.delete();
                    if (file.exists()) {
                        log.error("Upload process failed and failed to delete the uploaded file due to unexpected error");
                    }
                } catch (SecurityException ex) {
                    log.error("Upload process failed and failed to delete the uploaded file due to a SecurityException");
                }
                if (e instanceof ServiceException) {
                    throw (ServiceException) e;
                }
                else {
                    throw new ServiceException("500", "Upload failed");
                }
            }
        }
    }
}
