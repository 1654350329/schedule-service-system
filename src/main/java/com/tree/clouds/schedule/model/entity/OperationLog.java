package com.tree.clouds.schedule.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("operation_log")
@ApiModel(value = "操作日志分页", description = "操作日志")
@ColumnWidth(20)
public class OperationLog extends BaseEntity {

    public static final String OPERATION_ID = "operation_id";
    public static final String IP = "ip";
    public static final String OPERATION = "operation";
    private static final long serialVersionUID = 1L;

    @ExcelProperty("操作时间")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT, value = CREATED_TIME)
    public String createdTime;
    @ExcelProperty("操作人")
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT, value = CREATED_USER)
    public String createdUser;
    @ExcelIgnore
    @ApiModelProperty(value = "操作id")
    @TableId(value = "operation_id", type = IdType.UUID)
    private String operationId;
    @ExcelProperty("ip地址")
    @ApiModelProperty(value = "ip地址")
    @TableField("ip")
    private String ip;
    @ExcelProperty("操作")
    @ApiModelProperty(value = "操作")
    @TableField("operation")
    private String operation;
}
