package com.tree.clouds.schedule.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuVO {

    @ApiModelProperty("菜单ID")
    private String id;

    @NotNull(message = "上级菜单不能为空")
    @ApiModelProperty("上级菜单 父菜单ID，一级菜单为0")
    private String parentId;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("授权 多个用逗号分隔如：user:list,user:create")
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    @ApiModelProperty("菜单类型 0：目录   1:按钮")
    private Integer type;

    @TableField(exist = false)
    private List<SysMenuVO> children = new ArrayList<>();
}
