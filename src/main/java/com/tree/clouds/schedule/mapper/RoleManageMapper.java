package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.vo.RoleManagePageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色管理表 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface RoleManageMapper extends BaseMapper<RoleManage> {

    IPage<RoleManage> roleManagePage(IPage<RoleManage> page, @Param("roleManagePageVO") RoleManagePageVO roleManagePageVO);
}
