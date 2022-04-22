package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceScheduleVO {

    @ApiModelProperty(value = "任务id 新增不传 更新传")
    private String scheduleId;

    @ApiModelProperty(value = "设备id")
    private String deviceId;

    @ApiModelProperty(value = "通道号")
    private Long channel;
    @ApiModelProperty(value = "预置点")
    private Integer prefab;
}
