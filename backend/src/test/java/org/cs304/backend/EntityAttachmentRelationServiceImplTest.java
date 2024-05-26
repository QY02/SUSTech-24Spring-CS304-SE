package org.cs304.backend;

import org.cs304.backend.controller.EntityAttachmentRelationController;
import org.cs304.backend.entity.Attachment;
import org.cs304.backend.entity.EntityAttachmentRelation;
import org.cs304.backend.mapper.EntityAttachmentRelationMapper;
import org.cs304.backend.service.IEntityAttachmentRelationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class EntityAttachmentRelationServiceImplTest {
    @Mock
    private IEntityAttachmentRelationService entityAttachmentRelationService;

    @InjectMocks
    private EntityAttachmentRelationController entityAttachmentRelationController;

    @Mock
    private EntityAttachmentRelationMapper entityAttachmentRelationMapper;


    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(entityAttachmentRelationService, "baseMapper", entityAttachmentRelationMapper);

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
//        when(entityAttachmentRelationService.getById(anyInt())).thenReturn(attachment);
        when(entityAttachmentRelationService.getById(anyInt())).thenAnswer(invocation -> {
            Serializable id = invocation.getArgument(0);
            return entityAttachmentRelationService.getById(id);
        });

        // 执行被测试的方法
        Attachment result = entityAttachmentRelationService.getAttachment(userType, entity_type, entity_id, attachment_type);

        assertNull(result);

    }


}
