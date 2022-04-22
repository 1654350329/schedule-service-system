package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DistributeRoleVO {
    @ApiModelProperty("角色id")
    private String roleId;
    @ApiModelProperty("菜单id")
    private List<String> menuIds;
}
