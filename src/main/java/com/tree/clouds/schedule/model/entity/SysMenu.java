package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lzk
 * @since 2021-04-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    @ApiModelProperty("菜单id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @NotNull(message = "上级菜单不能为空")
    @ApiModelProperty("上级菜单 父菜单ID，一级菜单为0")
    private String parentId;

    @NotBlank(message = "菜单名称不能为空")
    @ApiModelProperty("菜单名称")
    private String name;


    @ApiModelProperty("菜单URL")
    private String path;

    @ApiModelProperty("授权 多个用逗号分隔如：user:list,user:create")
    @NotBlank(message = "菜单授权码不能为空")
    private String perms;

    private String component;

    @NotNull(message = "菜单类型不能为空")
    @ApiModelProperty("菜单类型 0：目录   1:按钮")
    private Integer type;

    @ApiModelProperty("菜单图标")
    private String icon;


    @TableField("orderNum")
    @ApiModelProperty("排序")
    private Integer orderNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
