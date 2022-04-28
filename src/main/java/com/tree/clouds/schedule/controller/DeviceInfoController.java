package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.vo.DeviceInfoPageVO;
import com.tree.clouds.schedule.model.vo.DeviceInfoVO;
import com.tree.clouds.schedule.model.vo.PublicIdsReqVO;
import com.tree.clouds.schedule.service.DeviceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 设备信息 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/device-info")
@Api(value = "device-info", tags = "设备信息管理模块")
public class DeviceInfoController {
    @Autowired
    private DeviceInfoService deviceInfoService;

    @PostMapping("/deviceInfoPage")
    @ApiOperation(value = "设备信息分页查询")
    @Log("设备信息分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<DeviceInfo>> deviceInfoPage(@RequestBody DeviceInfoPageVO deviceInfoPageVO) {
        IPage<DeviceInfo> page = deviceInfoService.deviceInfoPage(deviceInfoPageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addDevice")
    @ApiOperation(value = "添加设备")
    @Log("添加设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> addDevice(@RequestBody DeviceInfoVO deviceInfoVO) {
        deviceInfoService.addDevice(deviceInfoVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/importDevice")
    @ApiOperation(value = "导入设备")
    @Log("导入设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> importDevice(@RequestParam("file") MultipartFile file) {
        deviceInfoService.importDevice(file);
        return RestResponse.ok(true);
    }

    @GetMapping("/exportDevice")
    @ApiOperation(value = "导出设备")
    @Log("导出设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public void exportDevice(PublicIdsReqVO publicIdsReqVO, HttpServletResponse response) {
        deviceInfoService.exportDevice(publicIdsReqVO.getIds(), response);
    }

    @PostMapping("/updateDevice")
    @ApiOperation(value = "修改设备")
    @Log("添加设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> updateDevice(@RequestBody DeviceInfoVO deviceInfoVO) {
        deviceInfoService.updateDevice(deviceInfoVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteDevice")
    @ApiOperation(value = "删除设备")
    @Log("删除设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> deleteDevice(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        deviceInfoService.deleteDevice(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

}

