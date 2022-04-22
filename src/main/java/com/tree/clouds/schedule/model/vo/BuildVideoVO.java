package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BuildVideoVO {
    @ApiModelProperty("计划id")
    private String scheduleId;
    @ApiModelProperty("设备id")
    private String deviceId;
    @ApiModelProperty("日期")
    private List<String> data;
    @ApiModelProperty("背景音乐")
    private String musicId;
    @ApiModelProperty("帧率")
    private Integer fps;

}
