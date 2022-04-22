package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.bo.UserManageBO;
import com.tree.clouds.schedule.model.vo.PublicIdsReqVO;
import com.tree.clouds.schedule.model.vo.UpdatePasswordVO;
import com.tree.clouds.schedule.model.vo.UserManagePageVO;
import com.tree.clouds.schedule.model.vo.UserStatusVO;
import com.tree.clouds.schedule.service.UserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户管理 前端控制器
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/user-manage")
@Api(value = "user-manage", tags = "用户管理模块")
public class UserManageController {

    @Autowired
    private UserManageService userManageservice;


    @Log("用户模块分页查询")
    @PostMapping("/userManagePage")
    @ApiOperation(value = "用户模块分页查询")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<UserManageBO>> userManagePage(@Validated @RequestBody UserManagePageVO userManagePageVO) {
        IPage<UserManageBO> page = userManageservice.userManagePage(userManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addUserManage")
    @ApiOperation(value = "添加用户")
    @Log("添加用户")
    @PreAuthorize("hasAuthority('user:manage:add')")
    public RestResponse<Boolean> addUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userManageservice.addUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateUserManage")
    @ApiOperation(value = "修改用户")
    @Log("修改用户")
    @PreAuthorize("hasAuthority('user:manage:update')")
    public RestResponse<Boolean> updateUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userManageservice.updateUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteUserManage")
    @ApiOperation(value = "刪除用户")
    @Log("刪除用户")
    @PreAuthorize("hasAuthority('user:manage:delete')")
    public RestResponse<Boolean> deleteUserManage(@Validated @RequestBody PublicIdsReqVO publicIdReqVO) {
        for (String id : publicIdReqVO.getIds()) {
            userManageservice.deleteUserManage(id);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/rebuildPassword")
    @ApiOperation(value = "重置密码")
    @Log("重置密码")
    @PreAuthorize("hasAuthority('user:manage:rebuild')")
    public RestResponse<Boolean> rebuildPassword(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        userManageservice.rebuildPassword(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/userStatus")
    @ApiOperation(value = "启用或停用用户")
    @Log("启用或停用用户")
    @PreAuthorize("hasAuthority('user:manage:status')")
    public RestResponse<Boolean> userStatus(@Validated @RequestBody UserStatusVO userStatusVO) {
        userManageservice.userStatus(userStatusVO.getIds(), userStatusVO.getStatus());
        return RestResponse.ok(true);
    }

    @PostMapping("/importUser")
    @ApiOperation(value = "导入用户")
    @Log("导入用户")
    @PreAuthorize("hasAuthority('user:manage:import')")
    public RestResponse<Boolean> importUser(@RequestParam("file") MultipartFile file) {
        userManageservice.importUser(file);
        return RestResponse.ok(true);
    }

    @GetMapping("/exportUser")
    @ApiOperation(value = "导出用户")
    @Log("导出用户")
    public void exportUser(PublicIdsReqVO publicIdsReqVO, HttpServletResponse response) {
        userManageservice.exportUser(publicIdsReqVO.getIds(), response);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改当前用户密码")
    @Log("修改当前用户密码")
    public RestResponse<Boolean> updatePassword(@RequestBody UpdatePasswordVO updatePasswordVO) {
        userManageservice.updatePassword(updatePasswordVO);
        return RestResponse.ok(true);
    }
}

