package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 分组与角色管理中间表
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("group_user")
@ApiModel(value = "GroupRole对象", description = "分组与用户管理中间表")
public class GroupUser extends BaseEntity implements Serializable {

    public static final String GROUP_ID = "GROUP_ID";
    public static final String USER_ID = "user_id";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "分组id")
    @TableField(GROUP_ID)
    private String groupId;

    @ApiModelProperty(value = "用户id")
    @TableField(USER_ID)
    private String userId;


}
