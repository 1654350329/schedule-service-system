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
 * 角色管理表
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_manage")
@ApiModel(value = "角色管理分页", description = "角色管理分页")
public class RoleManage extends BaseEntity implements Serializable {

    public static final String ROLE_ID = "ROLE_ID";
    public static final String ROLE_NAME = "ROLE_NAME";
    public static final String ROLE_CODE = "ROLE_CODE";
    public static final String ROLE_MANAGER = "管理员";
    public static final String ROLE_UP_USER = "区县上报用户";
    public static final String ROLE_APPRAISE = "劳动力鉴定科经办";
    public static final String ROLE_EXPERT = "鉴定专家";
    public static final String ROLE_REVIEW_EXPERT_ONE = "一核专家";
    public static final String ROLE_REVIEW_EXPERT_TWO = "二核专家";
    public static final String ROLE_EXPERT_MANAGER = "总鉴定专家";
    public static final String ROLE_EXPERT_REVIEW = "审签领导";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "角色id")
    @TableId(value = ROLE_ID, type = IdType.UUID)
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    @TableField(ROLE_NAME)
    private String roleName;

    @ApiModelProperty(value = "角色code")
    @TableField(ROLE_CODE)
    private String roleCode;


}
