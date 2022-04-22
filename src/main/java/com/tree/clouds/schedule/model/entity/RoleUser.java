package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 角色与用户管理中间表
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_user")
@ApiModel(value = "RoleUser对象", description = "角色与用户管理中间表")
public class RoleUser extends BaseEntity implements Serializable {

    public static final String ROLE_ID = "ROLE_ID";
    public static final String USER_ID = "USER_ID";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "角色id")
    @TableId(value = ROLE_ID, type = IdType.UUID)
    private String roleId;

    @ApiModelProperty(value = "用户id")
    @TableField(USER_ID)
    private String userId;


}
