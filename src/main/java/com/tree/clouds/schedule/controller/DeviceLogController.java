package com.tree.clouds.schedule.controller;


import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.DeviceLog;
import com.tree.clouds.schedule.model.vo.PublicIdReqVO;
import com.tree.clouds.schedule.service.DeviceLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 设备日志 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-04-06
 */
@RestController
@RequestMapping("/device-log")
@Api(value = "device-log", tags = "设备日志模块")
public class DeviceLogController {
    @Autowired
    private DeviceLogService deviceLogService;

    @PostMapping("/deviceLog")
    @ApiOperation(value = "设备日志")
    @Log("设备日志")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<List<DeviceLog>> deviceLog(@RequestBody PublicIdReqVO publicIdReqVO) {
        List<DeviceLog> deviceLogs = deviceLogService.deviceLog(publicIdReqVO.getId());
        return RestResponse.ok(deviceLogs);
    }

}

