package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.entity.RoleUser;
import com.tree.clouds.schedule.model.entity.UserManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色与用户管理中间表 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
     * 根据角色获取用户信息
     *
     * @param roleName
     * @return
     */
    List<UserManage> getUserInfoByRole(@Param("roleName") String roleName);

    List<RoleManage> getRoleByUserId(@Param("userId") String userId);
}
