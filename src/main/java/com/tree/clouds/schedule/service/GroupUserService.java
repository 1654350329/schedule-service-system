package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.GroupUser;

/**
 * <p>
 * 分组与角色管理中间表 服务类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface GroupUserService extends IService<GroupUser> {

    void saveGroupUser(String userId, String groupId);

    void removeUserByUserId(String userId);
}
