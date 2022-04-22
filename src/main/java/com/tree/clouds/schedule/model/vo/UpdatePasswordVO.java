package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordVO {
    @NotBlank(message = "密码不许为空")
    @ApiModelProperty("用户密码")
    private String password;
}
