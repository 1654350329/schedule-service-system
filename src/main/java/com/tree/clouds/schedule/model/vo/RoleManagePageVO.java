package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoleManagePageVO extends PageParam {
    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
