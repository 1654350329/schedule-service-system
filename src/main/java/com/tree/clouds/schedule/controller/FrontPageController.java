package com.tree.clouds.schedule.controller;


import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备计划中间表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/front-page")
@Api(value = "front-page", tags = "首页数据")
public class FrontPageController {
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private AlbumRecordService albumRecordService;
    @Autowired
    private MusicManageService musicManageService;

    @PostMapping("/frontData")
    @ApiOperation(value = "首页数据")
    @Log("首页数据")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Map<String, Object>> frontData() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Map<String, Object>> typeMap = deviceInfoService.getTypePercentage();
        map.put("deviceType", typeMap);
        //设备数量
        map.put("deviceNumber", deviceInfoService.getDeviceSum());
        //任务计划总数
        int taskSum = scheduleTaskService.getTaskSum(null);
        map.put("taskSum", taskSum);
        //图片总数
        int imageSum = imageInfoService.getImageSum(null);
        map.put("imageSum", imageSum);
        //视频总数
        int recordSum = albumRecordService.getRecordSum(null);
        map.put("recordSum", recordSum);
        //音乐总数
        int musicSum = musicManageService.getMusicSum(null);
        map.put("musicSum", musicSum);
        //启用计划个数
        int startTaskSum = scheduleTaskService.getTaskSum(1);
        map.put("startTaskSum", startTaskSum);
        //停用计划个数
        map.put("stopTaskSum", taskSum - startTaskSum);
        //再用音乐
        int musicSumTrue = musicManageService.getMusicSum(1);
        map.put("musicSumTrue", musicSumTrue);
        //未用音乐
        map.put("musicSumFalse", musicSum - musicSumTrue);
        //设备异常列表
        List<DeviceInfo> errorList = deviceInfoService.getList(0);
        map.put("errorList", errorList);
        //正常设备列表
        List<DeviceInfo> normalList = deviceInfoService.getList(1);
        map.put("normalList", normalList);
        return RestResponse.ok(map);
    }


}

