package org.eventCenter.fileServer.service.impl;

import org.eventCenter.fileServer.component.GlobalData;
import org.eventCenter.fileServer.exception.ServiceException;
import org.eventCenter.fileServer.service.IFileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FileServiceImpl implements IFileService {
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
            return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).header("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8)).body(new InputStreamResource(new FileInputStream(file)));
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
}
