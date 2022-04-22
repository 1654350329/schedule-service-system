package com.tree.clouds.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.RoleUserMapper;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.entity.RoleUser;
import com.tree.clouds.schedule.service.RoleUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色与用户管理中间表 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Override
    public void addRole(List<String> roleIds, String userId) {
        for (String roleId : roleIds) {
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(roleId);
            roleUser.setUserId(userId);
            this.save(roleUser);
        }
    }

    @Override
    public List<RoleManage> getRoleByUserId(String userId) {
        return this.baseMapper.getRoleByUserId(userId);
    }

    @Override
    public boolean removeRole(String userId) {
        QueryWrapper<RoleUser> wrapper = new QueryWrapper<>();
        wrapper.eq(RoleUser.USER_ID, userId);
        return this.remove(wrapper);
    }
}
