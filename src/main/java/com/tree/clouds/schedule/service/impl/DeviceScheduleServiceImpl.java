package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.jna.NativeLong;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.common.ffmpeg.Move;
import com.tree.clouds.schedule.common.hkws.MonitorCameraInfo;
import com.tree.clouds.schedule.common.hkws.TestHikvision;
import com.tree.clouds.schedule.mapper.DeviceScheduleMapper;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;
import com.tree.clouds.schedule.model.vo.DeviceScheduleVO;
import com.tree.clouds.schedule.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备计划中间表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Service
@Slf4j
public class DeviceScheduleServiceImpl extends ServiceImpl<DeviceScheduleMapper, DeviceSchedule> implements DeviceScheduleService {

    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private AlbumRecordService albumRecordService;
    @Autowired
    private MusicManageService musicManageService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private DeviceLogService deviceLogService;
    @Autowired
    private ImageInfoService imageInfoService;

    @Override
    public void configureSchedule(DeviceScheduleVO deviceScheduleVO) {
        DeviceSchedule one = this.getOne(new QueryWrapper<DeviceSchedule>()
                .eq(DeviceSchedule.DEVICE_ID, deviceScheduleVO.getDeviceId())
                .eq(DeviceSchedule.SCHEDULE_ID, deviceScheduleVO.getScheduleId()));
        DeviceSchedule deviceSchedule = BeanUtil.toBean(deviceScheduleVO, DeviceSchedule.class);
        if (one != null) {
            one.setChannel(deviceScheduleVO.getChannel());
            one.setPrefab(deviceScheduleVO.getPrefab());
            this.updateById(one);
        } else {
            this.save(deviceSchedule);
        }
    }

    @Override
    public List<ConfigureDetailVO> getConfigureDetail(String scheduleId, List<String> deviceIds) {
        return this.baseMapper.getConfigureDetail(scheduleId, deviceIds);
    }

    @Override
    public List<DeviceSchedule> getByScheduleId(String scheduleId) {
        return this.list(new QueryWrapper<DeviceSchedule>().eq(DeviceSchedule.SCHEDULE_ID, scheduleId));
    }

    @Override
    public Boolean startSchedule(ScheduleTask scheduleTask, String scheduleCycle) {
        //开启归档任务
        archiveTask(scheduleTask);
        //开启抓拍任务
        List<DeviceSchedule> deviceSchedules = this.getByScheduleId(scheduleTask.getScheduleId());
        String date = scheduleTask.getEndDate();

        //判断是否到截止日期
        Date parse = DateUtil.parse(date, "yyyy-MM-dd");
        long time = parse.getTime();
        for (DeviceSchedule deviceSchedule : deviceSchedules) {
            captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, time);
        }
        return true;
    }

    public void captureTask(DeviceSchedule deviceSchedule, short codeRage, String scheduleCycle, int type, Long time) {
        ScheduleTask task = scheduleTaskService.getById(deviceSchedule.getScheduleId());
        DeviceInfo deviceInfo = this.deviceInfoService.getById(deviceSchedule.getDeviceId());
        if (deviceInfo == null || deviceInfo.getDel() != 0) {
            return;
        }
        MonitorCameraInfo cameraInfo = new MonitorCameraInfo();
        cameraInfo.setCameraIp(deviceInfo.getDeviceAddress());
        cameraInfo.setCameraPort(deviceInfo.getPort());
        cameraInfo.setUserName(deviceInfo.getDeviceAccount());
        cameraInfo.setUserPwd(deviceInfo.getDevicePassword());
        cameraInfo.setTaskId(deviceSchedule.getTaskId());
        cameraInfo.setDwPresetIndex(deviceSchedule.getPrefab());
        //设置通道号，其中1正常，-1不正常
        NativeLong chanLong = new NativeLong(deviceSchedule.getChannel());
        cameraInfo.setChannel(chanLong);
        cameraInfo.setWPicQuality(codeRage);
        cameraInfo.setPath(deviceInfo.getAddress() + "__周期:" + scheduleCycle);
        cameraInfo.setDeviceId(deviceInfo.getDeviceId());
        cameraInfo.setCreatUser(task.getCreatedUser());
        //抓拍1小时
        TestHikvision testHikvision = new TestHikvision(cameraInfo, task, time, deviceLogService, imageInfoService);
        String schedule = CronUtil.schedule(scheduleCycle, testHikvision);
        //判断是否到结束日期 结束移除任务
        String date = task.getEndDate();
        DateTime parse = DateUtil.parse(date, "yyyy-MM-dd");
        if (parse.getTime() > new Date().getTime()) {
            CronUtil.remove(task.getScheduleNumber());
        }

        //存放任务队列信息
        Constants.scheduleMap.put(deviceSchedule.getTaskId(), schedule);

    }

    public void archiveTask(ScheduleTask scheduleTask) {
        String musicPath = null;
        DateTime dateTime = DateUtil.parseDateTime(scheduleTask.getEndDate() + " 23:59:59");
        dateTime = DateUtil.offsetDay(dateTime, 1);
        if (scheduleTask.getMusicId() != null) {
            musicPath = musicManageService.getById(scheduleTask.getMusicId()).getFilePath();
        }
        String cron = null;
        //日出计划 与黑夜计划归档时间
        if ((scheduleTask.getTaskType() == 0 || scheduleTask.getTaskType() == 6) && scheduleTask.getCycle() == 0) {
            cron = "0 0 8 */1 * ? *";
        }
        //日落计划 与白天计划归档时间
        if ((scheduleTask.getTaskType() == 1 || scheduleTask.getTaskType() == 4) && scheduleTask.getCycle() == 0) {
            cron = "0 0 20 */1 * ? *";
        }
        if (scheduleTask.getTaskType() == 2 && scheduleTask.getCycle() == 0) {
            cron = "0 0 1 */1 * ? *";
        }
        if (scheduleTask.getTaskType() == 3 && scheduleTask.getCycle() == 0) {
            cron = "0 0 8 */1 * ? *";
        }
        if (scheduleTask.getTaskType() == 7 && scheduleTask.getCycle() == 0) {
            cron = String.format("0 0 %s */1 * ? *", Integer.parseInt(scheduleTask.getEndTime().split(":")[0]) + 1);
        }
        if (scheduleTask.getCycle() == 1) {
            cron = "0 0 0 ? * MON";
        }
        if (scheduleTask.getCycle() == 2) {
            cron = "0 0 0 1 * ?";
        }
        if (scheduleTask.getCycle() == 3) {
            cron = "0 0 0 1 1 ? *";
        }
        List<DeviceSchedule> deviceSchedules = this.getByScheduleId(scheduleTask.getScheduleId());
        for (DeviceSchedule deviceSchedule : deviceSchedules) {
            log.info("归档周期 = " + cron + " 任务号:" + deviceSchedule.getTaskId());
            String schedule = CronUtil.schedule(cron, new Move(scheduleTask, deviceSchedule.getTaskId(), dateTime, musicPath, albumRecordService, imageInfoService));
            //存放任务队列信息
            Constants.taskMap.put(deviceSchedule.getTaskId(), schedule);
        }
        if (!CronUtil.getScheduler().isStarted()) {
            // 支持秒级别定时任务
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }
    }

    @Override
    public DeviceSchedule getByScheduleIdAndDeviceId(String scheduleId, String deviceId) {
        return this.getOne(new QueryWrapper<DeviceSchedule>().eq(DeviceSchedule.SCHEDULE_ID, scheduleId)
                .eq(DeviceSchedule.DEVICE_ID, deviceId).orderByAsc(DeviceSchedule.CREATED_TIME));
    }

    @Override
    public Boolean stopSchedule(String scheduleId) {
        ScheduleTask scheduleTask = scheduleTaskService.getById(scheduleId);
        //移除抓拍每日执行任务
        if (scheduleTask.getScheduleNumber() != null) {
            CronUtil.remove(scheduleTask.getScheduleNumber());
        }
        List<DeviceSchedule> tasks = getByScheduleId(scheduleId);
        for (DeviceSchedule task : tasks) {
            //移除抓拍任务
            if (Constants.scheduleMap.containsKey(task.getTaskId())) {
                CronUtil.remove(Constants.scheduleMap.get(task.getTaskId()));
            }
            //移除归档任务
            if (Constants.taskMap.containsKey(task.getTaskId())) {
                CronUtil.remove(Constants.taskMap.get(task.getTaskId()));
            }
        }
        return true;
    }


}
