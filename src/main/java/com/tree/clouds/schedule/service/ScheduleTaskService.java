package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.vo.*;

import java.util.List;

/**
 * <p>
 * 计划配置 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface ScheduleTaskService extends IService<ScheduleTask> {

    void updateSchedule(ScheduleTaskVO scheduleTaskVO);

    IPage<ScheduleTaskBO> schedulePage(ScheduleTaskPageVO scheduleTaskPageVO);

    void addSchedule(ScheduleTaskVO scheduleTaskVO);

    void deleteSchedule(List<String> ids);

    void configureSchedule(List<DeviceScheduleVO> deviceScheduleVOS);

    List<ConfigureDetailVO> configureDetail(String scheduleId, List<String> deviceIds);

    void startSchedule(String scheduleId);

    void stopSchedule(String scheduleId);

    Boolean deleteConfigureDevice(String scheduleId, List<String> ids);

    int getTaskSum(Integer type);
}
