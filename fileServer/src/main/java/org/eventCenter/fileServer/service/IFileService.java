package org.eventCenter.fileServer.service;

import org.springframework.http.ResponseEntity;

public interface IFileService {
    ResponseEntity<?> download(String filePath);
}
