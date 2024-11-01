package org.cs304.backend.service;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAttachmentService extends IService<Attachment> {

    Attachment getById(int userType, Integer id);

    @Transactional(rollbackFor = {Exception.class})
    void deleteById(int userType, Integer id);

    JSONObject uploadStart(int userType, String fileDir);

    @Transactional(rollbackFor = {Exception.class})
    JSONObject uploadFinish(int userType, String filePath);

    JSONObject uploadBatchStart(int userType, List<String> fileDirList, List<String> fileNameList);

    @Transactional(rollbackFor = {Exception.class})
    List<JSONObject> uploadBatchFinish(int userType, List<String> filePathList);

    @Transactional(rollbackFor = {Exception.class})
    void deleteBatchByIdList(int userType, List<Integer> idList);
}
