package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserManagePageVO extends PageParam {
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "分组id")
    private String groupId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
