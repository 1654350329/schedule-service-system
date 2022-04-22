package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImageInfoVO {
    @ApiModelProperty("计划id")
    private String scheduleId;
    @ApiModelProperty("设备id")
    private String deviceId;
    @ApiModelProperty(value = "年")
    private String year;

    @ApiModelProperty(value = "月")
    private String month;

    @ApiModelProperty(value = "日")
    private String day;
}
