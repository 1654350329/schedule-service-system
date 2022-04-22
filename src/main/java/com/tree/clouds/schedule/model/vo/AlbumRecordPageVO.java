package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlbumRecordPageVO extends PageParam {
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty("计划id")
    private String scheduleId;
    @ApiModelProperty("设备id")
    private String deviceId;
}
