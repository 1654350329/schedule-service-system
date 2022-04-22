package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.GroupManage;
import com.tree.clouds.schedule.model.vo.GroupManagePageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 分组管理 Mapper 接口
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
public interface GroupManageMapper extends BaseMapper<GroupManage> {

    IPage<GroupManage> groupManagePage(IPage<GroupManage> page, @Param("groupManagePageVO") GroupManagePageVO groupManagePageVO);
}
