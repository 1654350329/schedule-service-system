package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.RoleManageMapper;
import com.tree.clouds.schedule.mapper.RoleUserMapper;
import com.tree.clouds.schedule.mapper.SysRoleMenuMapper;
import com.tree.clouds.schedule.model.entity.*;
import com.tree.clouds.schedule.model.vo.DistributeRoleVO;
import com.tree.clouds.schedule.model.vo.RoleManagePageVO;
import com.tree.clouds.schedule.service.RoleManageService;
import com.tree.clouds.schedule.service.SysMenuService;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色管理表 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class RoleManageServiceImpl extends ServiceImpl<RoleManageMapper, RoleManage> implements RoleManageService {

    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private UserManageService userManageService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public List<UserManage> getUserInfoByRole(String roleName) {
        return roleUserMapper.getUserInfoByRole(roleName);
    }

    @Override
    public IPage<RoleManage> roleManagePage(RoleManagePageVO roleManagePageVO) {
        IPage<RoleManage> page = roleManagePageVO.getPage();
        return this.baseMapper.roleManagePage(page, roleManagePageVO);
    }

    @Override
    @Transactional
    public void distributeRole(DistributeRoleVO distributeRoleVO) {
        this.sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq(SysRoleMenu.ROLE_ID, distributeRoleVO.getRoleId()));
        List<String> menuIds = distributeRoleVO.getMenuIds();
        Set<String> menuSet = new HashSet<>(menuIds);

        for (String menuId : menuIds) {
            SysMenu sysMenu = this.sysMenuService.getById(menuId);
            String pid = sysMenu.getParentId();
            while (!pid.equals("0")) {
                menuSet.add(pid);
                pid = this.sysMenuService.getById(pid).getParentId();
            }
        }
        for (String s : menuSet) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(s);
            sysRoleMenu.setRoleId(distributeRoleVO.getRoleId());
            this.sysRoleMenuMapper.insert(sysRoleMenu);
        }
        userManageService.clearUserAuthorityInfoByRoleId(distributeRoleVO.getRoleId());
    }

    @Override
    public void deleteRole(List<String> ids) {
        List<RoleUser> roleUsers = roleUserMapper.selectList(new QueryWrapper<RoleUser>().in(RoleUser.ROLE_ID, ids));
        if (CollUtil.isNotEmpty(roleUsers)) {
            throw new BaseBusinessException(400, "角色下存在用户,不许删除!!");
        }
        this.removeByIds(ids);
    }

}
