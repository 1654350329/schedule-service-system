package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;
import com.tree.clouds.schedule.model.vo.DeviceScheduleVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskPageVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskVO;
import org.springframework.web.multipart.MultipartFile;

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

    String uploadImage(MultipartFile file);
}
