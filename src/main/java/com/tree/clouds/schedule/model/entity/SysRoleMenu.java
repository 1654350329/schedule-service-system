package com.tree.clouds.schedule.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@Data
public class SysRoleMenu {

    public static final String ROLE_ID = "role_id";
    public static final String MENU_ID = "menu_id";

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    private String roleId;

    private String menuId;


}
