package org.cs304.backend;

import com.alibaba.fastjson2.JSONObject;
import org.cs304.backend.controller.AttachmentController;
import org.cs304.backend.entity.Attachment;
import org.cs304.backend.service.IAttachmentService;
import org.cs304.backend.utils.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class AttachmentControllerTest {

    @InjectMocks
    AttachmentController attachmentController;

    @Mock
    IAttachmentService attachmentService;

    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Test getById")
    public void testGetById() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 1);

        when(attachmentService.getById(anyInt(), anyInt())).thenReturn(new Attachment());

        Result result = attachmentController.getById(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test getBatchByIds")
    public void testGetBatchByIds() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("idList", List.of(1, 2, 3));

        when(attachmentService.getBatchByIds(anyInt(), any())).thenReturn(new ArrayList<>());

        Result result = attachmentController.getBatchByIds(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test uploadStart")
    public void testUploadStart() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("fileDir", "test");

        when(attachmentService.uploadStart(anyInt(), anyString(), any())).thenReturn(new JSONObject());

        Result result = attachmentController.uploadStart(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test uploadFinish")
    public void testUploadFinish() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("filePath", "test.txt");
        requestBody.put("requestData", new JSONObject());

        when(attachmentService.uploadFinish(anyInt(), anyString(), any(), anyBoolean())).thenReturn(new JSONObject());

        Result result = attachmentController.uploadFinish(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test uploadBatchStart")
    public void testUploadBatchStart() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("fileDirList", List.of("test", "test1", "test2"));
        requestBody.put("fileNameList", List.of("file.txt", "file1.txt", "file2.txt"));

        when(attachmentService.uploadBatchStart(anyInt(), any(), any(), any())).thenReturn(new JSONObject());

        Result result = attachmentController.uploadBatchStart(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test uploadBatchFinish")
    public void testUploadBatchFinish() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("filePathList", List.of("a/a.txt", "b/b.txt"));
        requestBody.put("requestData", new JSONObject());

        when(attachmentService.uploadBatchFinish(anyInt(), any(), any())).thenReturn(new JSONObject());

        Result result = attachmentController.uploadBatchFinish(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test deleteById")
    public void testDeleteById() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", 1);

        doNothing().when(attachmentService).deleteById(anyInt(), anyInt());

        Result result = attachmentController.deleteById(response, request, requestBody);
        assertEquals("200", result.getCode());
    }

    @Test
    @DisplayName("Test deleteBatchByIdList")
    public void testDeleteBatchByIdList() {
        request.setAttribute("loginUserType", 0);
        JSONObject requestBody = new JSONObject();
        requestBody.put("idList", List.of(1, 2, 3));

        doNothing().when(attachmentService).deleteBatchByIdList(anyInt(), any());

        Result result = attachmentController.deleteBatchByIdList(response, request, requestBody);
        assertEquals("200", result.getCode());
    }
}