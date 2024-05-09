package org.cs304.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.cs304.backend.entity.Attachment;
import org.cs304.backend.entity.EntityAttachmentRelation;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.AttachmentMapper;
import org.cs304.backend.mapper.EntityAttachmentRelationMapper;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.service.IEntityAttachmentRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cs304.backend.constant.constant_CommentType.BLOG;

@Service
public class EntityAttachmentRelationServiceImpl extends ServiceImpl<EntityAttachmentRelationMapper, EntityAttachmentRelation> implements IEntityAttachmentRelationService {
    @Resource
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;
    @Resource
    private AttachmentMapper attachmentMapper;

    @Resource
    private IAttachmentService attachmentService;

    @Override
    public Attachment getAttachment(int userType, int entity_type, int entity_id, int attachment_type) {
//        List<EntityAttachmentRelation> entityAttachmentRelation = entityAttachmentRelationMapper.selectList(
//                new QueryWrapper<EntityAttachmentRelation>().select("entity_type,entity_id,attachment_type").eq(entity_type,entity_id,attachment_type)
//
//        );
        List<EntityAttachmentRelation> entityAttachmentRelation = entityAttachmentRelationMapper.selectList(
                new QueryWrapper<EntityAttachmentRelation>()
                        .select("attachment_id")  // Adjusting to select only the attachment_id
                        .eq("entity_type", entity_type)
                        .eq("entity_id", entity_id)
                        .eq("attachment_type", attachment_type)
        );
        return attachmentService.getById(userType, entityAttachmentRelation.get(0).getAttachmentId());
//        Attachment attachment = attachmentMapper.selectById(entityAttachmentRelation.get(0).getAttachmentId());//我目前是只获得了第一个
//        if (attachment == null) {
//            throw new ServiceException("400", "Attachment not exist");
//        }
//        return attachment;

    }
}
