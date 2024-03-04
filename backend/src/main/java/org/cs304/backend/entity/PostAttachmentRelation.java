package org.cs304.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostAttachmentRelation", description = "")
public class PostAttachmentRelation {

    private Integer postType;

    private Integer postId;

    private Integer attachmentId;

    private String attachmentTitle;

    private Integer attachmentType;
}
