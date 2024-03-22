package org.cs304.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "OrderRecord", description = "")
public class OrderRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userId;

    private Integer eventId;

    private String seatId;

    private Integer status;

    private Integer price;

    private LocalDateTime submitTime;

    private LocalDateTime paymentTime;
}