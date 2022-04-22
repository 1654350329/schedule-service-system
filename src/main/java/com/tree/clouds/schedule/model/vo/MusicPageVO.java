package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MusicPageVO extends PageParam {
    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件格式")
    private String fileType;
}
