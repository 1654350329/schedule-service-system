package com.tree.clouds.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.GroupUserMapper;
import com.tree.clouds.schedule.model.entity.GroupUser;
import com.tree.clouds.schedule.service.GroupUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分组与角色管理中间表 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements GroupUserService {


    @Override
    public void saveGroupUser(String userId, String groupId) {
        GroupUser groupUser = new GroupUser();
        groupUser.setUserId(userId);
        groupUser.setGroupId(groupId);
        this.save(groupUser);
    }

    @Override
    public void removeUserByUserId(String userId) {
        QueryWrapper<GroupUser> wrapper = new QueryWrapper<>();
        wrapper.eq(GroupUser.USER_ID, userId);
        this.remove(wrapper);
    }

}
