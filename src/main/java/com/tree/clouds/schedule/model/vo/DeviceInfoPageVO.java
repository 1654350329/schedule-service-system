package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceInfoPageVO extends PageParam {
    @ApiModelProperty(value = "设备地址")
    private String deviceName;
    @ApiModelProperty(value = "设备地址")
    private String address;

    @ApiModelProperty(value = "设备账号")
    private String deviceAccount;

    @ApiModelProperty(value = "设备类型")
    private Integer deviceType;

    @ApiModelProperty(value = "计划id")
    private String scheduleId;
}
