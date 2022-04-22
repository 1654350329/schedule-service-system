package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserStatusVO {
    @NotNull(message = "状态不许为空")
    @ApiModelProperty("状态 0启用 1停用")
    private Integer status;

    @NotNull(message = "用户id不许为空")
    @ApiModelProperty("用户id")
    private List<String> ids;
}
