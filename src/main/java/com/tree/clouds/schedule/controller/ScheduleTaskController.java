package com.tree.clouds.schedule.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.vo.*;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.service.ScheduleTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 计划配置 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/schedule-task")
@Api(value = "schedule-task", tags = "计划配置管理模块")
public class ScheduleTaskController {
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private DeviceScheduleService deviceScheduleService;

    @PostMapping("/schedulePage")
    @ApiOperation(value = "计划配置分页查询")
    @Log("计划配置分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<ScheduleTaskBO>> schedulePage(@RequestBody ScheduleTaskPageVO scheduleTaskPageVO) {
        IPage<ScheduleTaskBO> page = scheduleTaskService.schedulePage(scheduleTaskPageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/configureSchedule")
    @ApiOperation(value = "配置设备")
    @Log("配置设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> configureSchedule(@RequestBody List<DeviceScheduleVO> deviceScheduleVOS) {
        scheduleTaskService.configureSchedule(deviceScheduleVOS);
        return RestResponse.ok(true);
    }

    @PostMapping("/getConfigureDetail/{scheduleId}")
    @ApiOperation(value = "获取配置设备详情")
    @Log("配置设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<List<ConfigureDetailVO>> getConfigureDetail(@RequestBody PublicIdsReqVO publicIdsReqVO, @PathVariable String scheduleId) {
        List<ConfigureDetailVO> configureDetailVOS = scheduleTaskService.configureDetail(scheduleId, publicIdsReqVO.getIds());
        return RestResponse.ok(configureDetailVOS);
    }

    @PostMapping("/deleteConfigureDevice/{scheduleId}")
    @ApiOperation(value = "移除配置设备")
    @Log("移除配置设备")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> deleteConfigureDevice(@RequestBody PublicIdsReqVO publicIdsReqVO, @PathVariable String scheduleId) {
        scheduleTaskService.deleteConfigureDevice(scheduleId, publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/addSchedule")
    @ApiOperation(value = "新增计划配置")
    @Log("新增计划配置查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> addSchedule(@RequestBody ScheduleTaskVO scheduleTaskVO) {
        if (scheduleTaskVO.getTaskType() == 2 && DateUtil.parseTime(scheduleTaskVO.getEndTime()).getTime() < DateUtil.parseTime(scheduleTaskVO.getStartTime()).getTime()) {
            //跨夜计划
            scheduleTaskVO.setTaskType(7);
        }
        scheduleTaskService.addSchedule(scheduleTaskVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateSchedule")
    @ApiOperation(value = "修改计划配置")
    @Log("修改计划配置")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> updateSchedule(@RequestBody ScheduleTaskVO scheduleTaskVO) {
        if (scheduleTaskVO.getTaskType() == 2 && DateUtil.parseTime(scheduleTaskVO.getEndTime()).getTime() < DateUtil.parseTime(scheduleTaskVO.getStartTime()).getTime()) {
            //跨夜计划
            scheduleTaskVO.setTaskType(7);
        }

        scheduleTaskService.updateSchedule(scheduleTaskVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteSchedule")
    @ApiOperation(value = "删除计划配置")
    @Log("删除计划配置")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> deleteSchedule(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        scheduleTaskService.deleteSchedule(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/startSchedule")
    @ApiOperation(value = "开启计划")
    @Log("开启计划")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> startSchedule(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        for (String id : publicIdsReqVO.getIds()) {
            scheduleTaskService.startSchedule(id);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/stopSchedule")
    @ApiOperation(value = "停止计划")
    @Log("停止计划")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> stopSchedule(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        for (String id : publicIdsReqVO.getIds()) {
            scheduleTaskService.stopSchedule(id);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/uploadImage")
    @ApiOperation(value = "上传水印图片")
    @Log("上传水印图片")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String image = scheduleTaskService.uploadImage(file);
        return RestResponse.ok(image);
    }
}

