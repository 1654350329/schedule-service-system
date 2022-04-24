package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.ScheduleTaskMapper;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;
import com.tree.clouds.schedule.model.vo.DeviceScheduleVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskPageVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskVO;
import com.tree.clouds.schedule.service.*;
import com.tree.clouds.schedule.task.SumRiseSetTask;
import com.tree.clouds.schedule.task.TaskSchedule;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 计划配置 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Service
@Slf4j
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTask> implements ScheduleTaskService {

    @Autowired
    private DeviceScheduleService deviceScheduleService;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private AlbumRecordService albumRecordService;

    @Override
    public IPage<ScheduleTaskBO> schedulePage(ScheduleTaskPageVO scheduleTaskPageVO) {
        IPage<ScheduleTaskBO> page = scheduleTaskPageVO.getPage();
        page = this.baseMapper.schedulePage(page, scheduleTaskPageVO);
        for (ScheduleTaskBO record : page.getRecords()) {
            if (scheduleTaskPageVO.getDeviceId() != null) {
                DeviceSchedule deviceSchedule = this.deviceScheduleService.getByScheduleIdAndDeviceId(record.getScheduleId(), scheduleTaskPageVO.getDeviceId());
                record.setImageNumber(this.imageInfoService.getImageSum(deviceSchedule.getTaskId()));
                record.setVideoNumber(this.albumRecordService.getRecordSum(deviceSchedule.getTaskId()));
            }
            if (record.getTaskType() == 2) {
                record.setTime(record.getStartTime() + " 至 " + record.getEndTime());
            }
            record.setDate(record.getStartDate() + " 至 " + record.getEndDate());
            int size = deviceScheduleService.getByScheduleId(record.getScheduleId()).size();
            record.setDeviceNumber(size);
        }
        return page;
    }

    @Override
    public void updateSchedule(ScheduleTaskVO scheduleTaskVO) {
        ScheduleTask task = this.getById(scheduleTaskVO.getScheduleId());
        ScheduleTask scheduleTask = BeanUtil.toBean(scheduleTaskVO, ScheduleTask.class);
        if (task.getScheduleStatus() != null && task.getScheduleStatus() == 1) {
            throw new BaseBusinessException(400, "任务已开启,请先停止任务!");
        }
        this.updateById(scheduleTask);
    }

    @Override
    public void addSchedule(ScheduleTaskVO scheduleTaskVO) {
        ScheduleTask scheduleTask = BeanUtil.toBean(scheduleTaskVO, ScheduleTask.class);
        scheduleTask.setScheduleStatus(0);
        this.save(scheduleTask);
    }

    @Override
    public void deleteSchedule(List<String> ids) {
        List<ScheduleTask> collect = this.listByIds(ids).stream().filter(scheduleTask -> scheduleTask.getScheduleStatus() != null && scheduleTask.getScheduleStatus() == 1).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            throw new BaseBusinessException(400, "任务已开启,请先终止!");
        }
        List<String> taskIds = new ArrayList<>();
        for (String id : ids) {
            List<DeviceSchedule> deviceSchedules = this.deviceScheduleService.getByScheduleId(id);
            for (DeviceSchedule deviceSchedule : deviceSchedules) {
                //删除图片
                FileUtil.del(Constants.SCHEDULE_PATH + deviceSchedule.getTaskId());
                //删除视频
                FileUtil.del(Constants.MP4_PATH + deviceSchedule.getTaskId());
                taskIds.add(deviceSchedule.getTaskId());
            }
        }
        deviceScheduleService.removeByIds(taskIds);
        this.removeByIds(ids);
    }

    @Override
    public void configureSchedule(List<DeviceScheduleVO> deviceScheduleVOS) {
        if (CollUtil.isEmpty(deviceScheduleVOS)) {
            throw new BaseBusinessException(400, "请先选择数据");
        }
        DeviceScheduleVO vo = deviceScheduleVOS.get(0);
        ScheduleTask task = this.getById(vo.getScheduleId());
        if (task.getScheduleStatus() != null && task.getScheduleStatus() == 1) {
            throw new BaseBusinessException(400, "任务已开启,请先停止任务!");
        }
        DeviceInfo deviceInfo = this.deviceInfoService.getById(vo.getDeviceId());
        if (vo.getPrefab() != null && deviceInfo.getDeviceType() != 1) {
            throw new BaseBusinessException(400, deviceInfo.getDeviceName() + "不支持设备预置点");
        }
        if (vo.getChannel() != null && vo.getChannel() > deviceInfo.getChannelNumber()) {
            throw new BaseBusinessException(400, deviceInfo.getDeviceName() + "通道不存在");
        }
        for (DeviceScheduleVO deviceScheduleVO : deviceScheduleVOS) {
            deviceScheduleService.configureSchedule(deviceScheduleVO);
        }
    }

    @Override
    public List<ConfigureDetailVO> configureDetail(String scheduleId, List<String> deviceIds) {
        return deviceScheduleService.getConfigureDetail(scheduleId, deviceIds);
    }

    @Override
    public void startSchedule(String scheduleId) {
        ScheduleTask scheduleTask = this.getById(scheduleId);
        String dateTime = scheduleTask.getEndDate() + " " + scheduleTask.getEndTime() + ":59:59";
        if (new Date().getTime() > DateUtil.parseDateTime(dateTime).getTime()) {
            return;
        }
        String schedule = null;
        //日出
        if (scheduleTask.getTaskType() == 0) {
            //执行时间
            String date = scheduleTask.getStartDate();
            DateTime parseDate = DateUtil.parseDateTime(scheduleTask.getStartDate() + " 05:09:00");
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDate(new Date());
            }
            String[] dates = date.split("-");
            String schedulingPattern = String.format("0 10 5 %s/1 %s ? %s", dates[2], dates[1], dates[0]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("日出计划schedulingPattern = " + schedulingPattern);
        }
        //日落
        if (scheduleTask.getTaskType() == 1) {
            //执行时间
            String date = scheduleTask.getStartDate();
            DateTime parseDate = DateUtil.parseDateTime(scheduleTask.getStartDate() + " 16:28:00");
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDate(new Date());
            }
            String[] dates = date.split("-");
            String schedulingPattern = String.format("0 30 16 %s/1 %s ? %s", dates[2], dates[1], dates[0]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("日落计划schedulingPattern = " + schedulingPattern);
        }
        //自定义时间
        if (scheduleTask.getTaskType() == 2) {
            //执行时间
            String date = scheduleTask.getStartDate() + " " + scheduleTask.getStartTime();
            DateTime parseDate = DateUtil.parseDateTime(date);
            date = DateUtil.formatDateTime(new Date(parseDate.getTime() - 1000 * 30));
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
            }
            String[] dates = date.replace(" ", "-").replaceAll(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s %s/1 %s ? %s", dates[5], dates[4], dates[3], dates[2], dates[1], dates[0]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("自定义计划schedulingPattern = " + schedulingPattern);
//            //首次执行时间
//            String schedulingPattern;
//            String date = scheduleTask.getStartDate();
//            DateTime parse = DateUtil.parse(date + " " + scheduleTask.getStartTime() + ":00:00", "yyyy-MM-dd HH:mm:ss");
//            if (parse.getTime() <= new Date().getTime()) {
//                oldTime = scheduleTask.getStartTime();
//                startTime = DateUtil.formatTime(new Date(new Date().getTime() + 10000));
//                date = DateUtil.formatDate(new Date());
//            } else {
//                startTime = scheduleTask.getStartTime() + ":00:00";
//            }
//            String[] dates = date.split("-");
//            String[] times = startTime.split(":");
//            //首次执行时间
//            schedulingPattern = String.format("%s %s %s %s %s ? *", times[2], times[1], times[0], dates[2], dates[1]);
//            if (oldTime != null) {
//                times[0] = String.valueOf(oldTime);
//            }
//            String scheduleCycle = null;
//
//            if (scheduleTask.getFrequencyUnit() == 0) {
//                scheduleCycle = String.format("*/%s * %s-%s * * ? *", scheduleTask.getFrequency(), times[0], scheduleTask.getEndTime());
//            }
//            if (scheduleTask.getFrequencyUnit() == 1) {
//                scheduleCycle = String.format("0 */%s %s-%s * * ? *", scheduleTask.getFrequency(), times[0], scheduleTask.getEndTime());
//            }
//            if (scheduleTask.getFrequencyUnit() == 2) {
//                scheduleCycle = String.format("0 0 %s-%s/%s * * ? *", scheduleTask.getFrequency(), times[0], scheduleTask.getEndTime());
//            }
//
//            System.out.println("schedulingPattern = " + schedulingPattern);
//            System.out.println("scheduleCycle = " + scheduleCycle);
//            TaskSchedule taskSchedule = new TaskSchedule(scheduleCycle, scheduleTask, this.deviceScheduleService);
//            String schedule = CronUtil.schedule(schedulingPattern, taskSchedule);
//            scheduleTask.setScheduleNumber(schedule);
        }

        if (!CronUtil.getScheduler().isStarted()) {
            // 支持秒级别定时任务
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }
        //更新状态
        ScheduleTask task = new ScheduleTask();
        task.setScheduleId(scheduleId);
        task.setScheduleNumber(schedule);
        task.setScheduleStatus(ScheduleTask.STATUS_TRUE);

        this.updateById(task);
    }

    @Override
    public void stopSchedule(String scheduleId) {
        this.deviceScheduleService.stopSchedule(scheduleId);
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setScheduleId(scheduleId);
        scheduleTask.setScheduleStatus(ScheduleTask.STATUS_FALSE);
        this.updateById(scheduleTask);
    }

    @Override
    public Boolean deleteConfigureDevice(String scheduleId, List<String> ids) {
        this.deviceScheduleService.remove(new QueryWrapper<DeviceSchedule>().eq(DeviceSchedule.SCHEDULE_ID, scheduleId)
                .in(DeviceSchedule.DEVICE_ID, ids));
        return true;
    }

    @Override
    public int getTaskSum(Integer type) {
        QueryWrapper<ScheduleTask> scheduleTaskQueryWrapper = new QueryWrapper<>();
        scheduleTaskQueryWrapper.eq(ScheduleTask.CREATED_USER, LoginUserUtil.getUserId());
        if (type != null) {
            scheduleTaskQueryWrapper.eq(ScheduleTask.SCHEDULE_STATUS, type);
        }
        return this.count(scheduleTaskQueryWrapper);
    }


}
