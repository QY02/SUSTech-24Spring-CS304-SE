package org.cs304.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cs304.backend.entity.EntityAttachmentRelation;
import org.cs304.backend.mapper.EntityAttachmentRelationMapper;
import org.cs304.backend.service.IEntityAttachmentRelationService;
import org.springframework.stereotype.Service;

@Service
public class EntityAttachmentRelationServiceImpl extends ServiceImpl<EntityAttachmentRelationMapper, EntityAttachmentRelation> implements IEntityAttachmentRelationService {

}
