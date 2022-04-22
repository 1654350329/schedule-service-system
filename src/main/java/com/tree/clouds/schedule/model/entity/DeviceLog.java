package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 设备日志
 * </p>
 *
 * @author LZK
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_log")
@ApiModel(value = "DeviceLog对象", description = "设备日志")
public class DeviceLog extends BaseEntity {
    public static final String LOG_ID = "log_id";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_STATUS = "device_status";
    public static final String LOG_INFO = "log_info";
    public static final String ERROR_CODE = "error_code";
    @ApiModelProperty(value = "日志id")
    @TableId(value = "log_id", type = IdType.UUID)
    private String logId;

    @ApiModelProperty(value = "设备主键")
    @TableField("device_id")
    private String deviceId;

    @ApiModelProperty(value = "设备状态")
    @TableField("device_status")
    private Integer deviceStatus;

    @ApiModelProperty(value = "设备日志")
    @TableField("log_info")
    private String logInfo;

    @ApiModelProperty(value = "错误代码")
    @TableField("error_code")
    private Integer errorCode;


}
