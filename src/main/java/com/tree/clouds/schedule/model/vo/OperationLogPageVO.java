package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationLogPageVO extends PageParam {
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "操作开始时间")
    private String StartTime;
    @ApiModelProperty(value = "操作结束时间")
    private String EndTime;
}
