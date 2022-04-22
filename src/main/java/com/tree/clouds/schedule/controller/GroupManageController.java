package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.GroupManage;
import com.tree.clouds.schedule.model.vo.GroupManagePageVO;
import com.tree.clouds.schedule.model.vo.PublicIdsReqVO;
import com.tree.clouds.schedule.service.GroupManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 分组管理 前端控制器
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/group-manage")
@Api(value = "group-manage", tags = "分组管理模块")
public class GroupManageController {

    @Autowired
    private GroupManageService groupManageService;

    @PostMapping("/groupManagePage")
    @ApiOperation(value = "分组管理模块分页查询")
    @Log("分组管理模块分页查询")
    @PreAuthorize("hasAuthority('group:manage:list')")
    public RestResponse<IPage<GroupManage>> groupManagePage(@RequestBody GroupManagePageVO groupManagePageVO) {
        IPage<GroupManage> page = groupManageService.groupManagePage(groupManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addGroupRole")
    @ApiOperation(value = "添加分组")
    @Log("添加分组")
    @PreAuthorize("hasAuthority('group:manage:add')")
    public RestResponse<Boolean> addGroupRole(@RequestBody GroupManage groupRole) {
        groupManageService.save(groupRole);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateGroupRole")
    @ApiOperation(value = "修改分组")
    @Log("修改分组")
    @PreAuthorize("hasAuthority('group:manage:update')")
    public RestResponse<Boolean> updateGroupRole(@RequestBody GroupManage groupRole) {
        groupManageService.updateById(groupRole);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteGroupRole")
    @ApiOperation(value = "刪除分组")
    @Log("刪除分组")
    @PreAuthorize("hasAuthority('group:manage:delete')")
    public RestResponse<Boolean> deleteGroupRole(@Validated @RequestBody PublicIdsReqVO publicIdReqVO) {
        groupManageService.deleteGroupRole(publicIdReqVO.getIds());
        return RestResponse.ok(true);
    }
}

