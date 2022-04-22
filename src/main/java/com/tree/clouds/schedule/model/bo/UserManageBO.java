package com.tree.clouds.schedule.model.bo;

import com.tree.clouds.schedule.model.entity.UserManage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserManageBO extends UserManage {
    @ApiModelProperty("角色id")
    @NotNull(message = "至少绑定一种角色")
    private List<String> roleIds;
    @ApiModelProperty("分组Id")
    @NotNull(message = "至少绑定一个分组")
    private String groupId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
