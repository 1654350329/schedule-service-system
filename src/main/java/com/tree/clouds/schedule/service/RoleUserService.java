package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.entity.RoleUser;

import java.util.List;

/**
 * <p>
 * 角色与用户管理中间表 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface RoleUserService extends IService<RoleUser> {

    void addRole(List<String> roleIds, String userId);

    List<RoleManage> getRoleByUserId(String userId);

    boolean removeRole(String userId);
}
