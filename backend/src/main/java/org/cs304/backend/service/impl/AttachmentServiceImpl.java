package org.cs304.backend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.cs304.backend.constant.constant_User;
import org.cs304.backend.entity.Attachment;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.AttachmentMapper;
import org.cs304.backend.service.IAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cs304.backend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

    @Resource
    private RedisUtil redisUtil;

    @Value("${file-server.admin-token:}")
    private String adminToken;

    @Override
    public Attachment getById(int userType, Integer id) {
        if (id == null) {
            throw new ServiceException("400", "Invalid Attachment Id");
        }
        if (userType != constant_User.ADMIN) {
            throw new ServiceException("401", "Permission denied");
        }
        Attachment attachment = baseMapper.selectById(id);
        if (attachment == null) {
            throw new ServiceException("400", "Attachment not exist");
        }
        attachment.setFilePath(redisUtil.generateAndAddFileToken(attachment.getFilePath()));
        return attachment;
    }

    @Override
    public void deleteById(int userType, Integer id) {
        if (id == null) {
            throw new ServiceException("400", "Invalid Attachment Id");
        }
        if (userType != constant_User.ADMIN) {
            throw new ServiceException("401", "Permission denied");
        }
        if (adminToken.isBlank()) {
            throw new ServiceException("500", "This API is currently unavailable");
        }
        Attachment attachment = baseMapper.selectById(id);
        if (attachment == null) {
            throw new ServiceException("400", "Attachment not exist");
        }
        baseMapper.deleteById(id);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        JSONObject requestBody = new JSONObject();
        requestBody.put("filePath", attachment.getFilePath());
        httpHeaders.set("token", adminToken);
        try {
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
            int statusCode = restTemplate.exchange("http://localhost:8084/file/delete", HttpMethod.POST, httpEntity, Void.class).getStatusCode().value();
            if (statusCode != 200) {
                throw new ServiceException("500", "An error occurred when communicating with the file server");
            }
        } catch (RestClientException e) {
            throw new ServiceException("500", "An error occurred when communicating with the file server");
        }
    }
}
