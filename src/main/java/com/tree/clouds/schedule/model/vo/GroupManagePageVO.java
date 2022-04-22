package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupManagePageVO extends PageParam {

    @ApiModelProperty(value = "分组名称")
    private String groupName;
}
