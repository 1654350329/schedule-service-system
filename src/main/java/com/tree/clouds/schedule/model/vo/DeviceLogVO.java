package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceLogVO {
    @ApiModelProperty(value = "设备日志")
    private String logInfo;

    @ApiModelProperty(value = "错误代码")
    private Integer errorCode;
}
