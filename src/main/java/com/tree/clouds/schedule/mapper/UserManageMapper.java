package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.bo.UserManageBO;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.model.vo.UserManagePageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户管理 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface UserManageMapper extends BaseMapper<UserManage> {

    IPage<UserManageBO> userManagePage(IPage<UserManageBO> page, @Param("userManagePageVO") UserManagePageVO userManagePageVO);

    List<UserManage> listByMenuId(@Param("menuId") String menuId);

    UserManage isExist(String account, String phone);

    List<UserManage> listByRoleId(@Param("roleId") String roleId);
}
