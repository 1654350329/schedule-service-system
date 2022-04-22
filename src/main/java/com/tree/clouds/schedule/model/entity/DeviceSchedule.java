package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 设备计划中间表
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_schedule")
@ApiModel(value = "DeviceSchedule对象", description = "设备计划中间表")
public class DeviceSchedule extends BaseEntity {
    public static final String TASK_ID = "task_id";

    public static final String DEVICE_ID = "device_id";

    public static final String SCHEDULE_ID = "schedule_id";

    @ApiModelProperty(value = "任务id")
    @TableId(value = "task_id", type = IdType.UUID)
    private String taskId;

    @ApiModelProperty(value = "设备id")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "计划id")
    @TableField("schedule_id")
    private String scheduleId;

    @ApiModelProperty(value = "通道号")
    @TableField("channel")
    private Long channel;

    @ApiModelProperty(value = "预置点")
    @TableField("prefab")
    private Integer prefab;


}
