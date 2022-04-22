package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginLogPageVO extends PageParam {

    @ApiModelProperty(value = "用户账号")
    private String account;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "登入开始时间")
    private String loginStartTime;
    @ApiModelProperty(value = "登入结束时间")
    private String loginEndTime;
}
