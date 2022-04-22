package com.tree.clouds.schedule.service;

import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;
import com.tree.clouds.schedule.model.vo.DeviceScheduleVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备计划中间表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface DeviceScheduleService extends IService<DeviceSchedule> {

    void configureSchedule(DeviceScheduleVO deviceScheduleVO);

    List<ConfigureDetailVO> getConfigureDetail(String scheduleId, List<String> deviceIds);

    List<DeviceSchedule> getByScheduleId(String scheduleId);

    Boolean startSchedule(ScheduleTask scheduleTask, String scheduleCycle);

    Boolean stopSchedule(String scheduleId);

    /**
     * 按任务抓拍图片
     *
     * @param deviceSchedule
     * @param codeRage
     * @param scheduleCycle
     * @param
     */
    void captureTask(DeviceSchedule deviceSchedule, short codeRage, String scheduleCycle, int type, Long date);

    void archiveTask(ScheduleTask scheduleTask);

    DeviceSchedule getByScheduleIdAndDeviceId(String scheduleId, String deviceId);
}
