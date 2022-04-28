package com.tree.clouds.schedule.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {
    public static final String CREATED_USER = "CREATED_USER";
    public static final String CREATED_TIME = "CREATED_TIME";
    public static final String UPDATED_USER = "UPDATED_USER";
    public static final String UPDATED_TIME = "UPDATED_TIME";
    public static final String DEL = "DEL";

    @ExcelIgnore
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT, value = CREATED_USER)
    public String createdUser;

    @ExcelIgnore
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT, value = CREATED_TIME)
    public String createdTime;

    @ExcelIgnore
    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE, value = UPDATED_USER)
    public String updatedUser;

    @ExcelIgnore
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE, value = UPDATED_TIME)
    public String updatedTime;

    @ExcelIgnore
    @ApiModelProperty(value = "删除")
    @TableField(fill = FieldFill.INSERT, value = DEL)
    public Integer del;
}
