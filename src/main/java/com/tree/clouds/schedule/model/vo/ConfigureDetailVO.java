package com.tree.clouds.schedule.model.vo;

import com.tree.clouds.schedule.model.entity.DeviceInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfigureDetailVO extends DeviceInfo {
    @ApiModelProperty(value = "通道号")
    private Long channel;

    @ApiModelProperty(value = "预置点")
    private Integer prefab;
}
