package org.cs304.backend.service;

import org.cs304.backend.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface IAttachmentService extends IService<Attachment> {

    Attachment getById(int userType, Integer id);

    @Transactional(rollbackFor = {Exception.class})
    void deleteById(int userType, Integer id);
}
