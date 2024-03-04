package org.cs304.backend.service.impl;

import org.cs304.backend.entity.Attachment;
import org.cs304.backend.mapper.AttachmentMapper;
import org.cs304.backend.service.IAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {

}
