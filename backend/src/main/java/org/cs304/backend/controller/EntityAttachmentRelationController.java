package org.cs304.backend.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.service.IEntityAttachmentRelationService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postAttachmentRelation")
public class EntityAttachmentRelationController {
    @Resource
    private IEntityAttachmentRelationService entityAttachmentRelationService;

    @PostMapping("/getAttachment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"entity_type\": 1,\"entity_id\": 1,\"attachment_type\": 0}")))
//    @Operation(summary = "通过entityId, entityType, attachType获得attach")
    public Result getAttachmentByEntityId(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        int userType = (int) request.getAttribute("loginUserType");
        Integer entity_type = requestBody.getInteger("entity_type");
        Integer entity_id = requestBody.getInteger("entity_id");
        Integer attachment_type = requestBody.getInteger("attachment_type");
        return Result.success(response, entityAttachmentRelationService.getAttachment(userType,entity_type, entity_id, attachment_type));
    }
}
