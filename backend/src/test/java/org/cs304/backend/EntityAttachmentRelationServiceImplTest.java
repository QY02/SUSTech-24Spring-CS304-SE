package org.cs304.backend;

import org.cs304.backend.service.impl.EntityAttachmentRelationServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.cs304.backend.entity.*;
import org.cs304.backend.mapper.*;
import org.cs304.backend.service.IAttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EntityAttachmentRelationServiceImplTest {
    @InjectMocks
    private EntityAttachmentRelationServiceImpl entityAttachmentRelationService;

    @Mock
    private IAttachmentService attachmentService;

    @Mock
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GetAttachment")
    public void testGetAttachment_Success() {
        // 准备测试数据
        int userType = 1;
        int entity_type = 1;
        int entity_id = 1;
        int attachment_type = 0;

        // 模拟数据库返回的附件ID列表
        List<EntityAttachmentRelation> entityAttachmentRelationList = new ArrayList<>();
        EntityAttachmentRelation entityAttachmentRelation = new EntityAttachmentRelation();
        entityAttachmentRelation.setAttachmentId(1); // 设置附件ID
        entityAttachmentRelationList.add(entityAttachmentRelation);
        when(entityAttachmentRelationMapper.selectList(any())).thenReturn(entityAttachmentRelationList);

        // 模拟附件服务返回附件
        Attachment attachment = new Attachment();
        attachment.setId(1);
        attachment.setFilePath("TestAttachment");
        when(attachmentService.getById(anyInt(), anyInt())).thenReturn(attachment);

        // 执行被测试的方法
        Attachment result = entityAttachmentRelationService.getAttachment(userType, entity_type, entity_id, attachment_type);
        assertNotNull(result);
    }
}
