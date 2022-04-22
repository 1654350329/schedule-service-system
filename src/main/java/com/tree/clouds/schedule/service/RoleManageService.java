package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.model.vo.DistributeRoleVO;
import com.tree.clouds.schedule.model.vo.RoleManagePageVO;


import java.util.List;

/**
 * <p>
 * 角色管理表 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface RoleManageService extends IService<RoleManage> {
    /**
     * 根据角色获取用户信息
     *
     * @param roleName
     * @return
     */
    List<UserManage> getUserInfoByRole(String roleName);

    IPage<RoleManage> roleManagePage(RoleManagePageVO roleManagePageVO);

    void distributeRole(DistributeRoleVO distributeRoleVO);

    void deleteRole(List<String> ids);
}
