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
 * 分组管理
 * </p>
 *
 * @author LZK
 * @since 2021-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("group_manage")
@ApiModel(value = "GroupManage对象", description = "分组管理")
public class GroupManage extends BaseEntity implements Serializable {

    public static final String GROUP_ID = "GROUP_ID";
    public static final String GROUP_NAME = "GROUP_NAME";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "分组id")
    @TableId(value = GROUP_ID, type = IdType.UUID)
    private String groupId;

    @ApiModelProperty(value = "分组名称")
    @TableField(GROUP_NAME)
    private String groupName;


}
