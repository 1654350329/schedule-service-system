package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.GroupManageMapper;
import com.tree.clouds.schedule.mapper.GroupUserMapper;
import com.tree.clouds.schedule.model.entity.GroupManage;
import com.tree.clouds.schedule.model.entity.GroupUser;
import com.tree.clouds.schedule.model.vo.GroupManagePageVO;
import com.tree.clouds.schedule.service.GroupManageService;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分组管理 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
public class GroupManageServiceImpl extends ServiceImpl<GroupManageMapper, GroupManage> implements GroupManageService {

    @Autowired
    private GroupUserMapper groupUserMapper;

    @Override
    public IPage<GroupManage> groupManagePage(GroupManagePageVO groupManagePageVO) {
        IPage<GroupManage> page = groupManagePageVO.getPage();
        return this.baseMapper.groupManagePage(page, groupManagePageVO);
    }

    @Override
    public void deleteGroupRole(List<String> ids) {
        List<GroupUser> groupUsers = groupUserMapper.selectList(new QueryWrapper<GroupUser>().in(GroupUser.GROUP_ID, ids));
        if (CollUtil.isNotEmpty(groupUsers)) {
            throw new BaseBusinessException(400, "分组存在用户,不许删除!");
        }
        this.removeByIds(ids);
    }
}
