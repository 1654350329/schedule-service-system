package com.tree.clouds.schedule.task;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.task.Task;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.DeviceInfoService;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.utils.SunRiseSet;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class SumRiseSetTask implements Task {
    private ScheduleTask scheduleTask;
    private DeviceScheduleService deviceScheduleService;
    private DeviceInfoService deviceInfoService;

    public SumRiseSetTask(ScheduleTask scheduleTask, DeviceScheduleService deviceScheduleService, DeviceInfoService deviceInfoService) {
        this.scheduleTask = scheduleTask;
        this.deviceScheduleService = deviceScheduleService;
        this.deviceInfoService = deviceInfoService;
    }

    @Override
    public void execute() {
        List<DeviceSchedule> deviceSchedules = deviceScheduleService.getByScheduleId(scheduleTask.getScheduleId());
        for (DeviceSchedule deviceSchedule : deviceSchedules) {
            DeviceInfo deviceInfo = deviceInfoService.getById(deviceSchedule.getDeviceId());
            String startTime = null;
            if (scheduleTask.getTaskType() == 0) {
                startTime = SunRiseSet.getSunrise(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), new Date());
                DateTime dateTime = DateUtil.parseDateTime(DateUtil.formatDate(new Date()) + " " + startTime);
                try {
                    String scheduleCycle = String.format("*/%s * * * * ? *", scheduleTask.getFrequency());
                    while (new Date().getTime() < dateTime.getTime()) {
                        System.out.println(" 等等执行" + deviceInfo);
                        Thread.currentThread().sleep(1000 * 30);
                    }
                    deviceScheduleService.captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, dateTime.getTime() + 1000 * 60 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (scheduleTask.getTaskType() == 1) {
                startTime = SunRiseSet.getSunset(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), new Date());
                //日出落前半小时
                DateTime dateTime = DateUtil.parseDateTime(DateUtil.formatDate(new Date()) + " " + startTime);
                try {
                    String scheduleCycle = String.format("*/%s * * * * ? *", scheduleTask.getFrequency());
                    while (new Date().getTime() < dateTime.getTime()) {
                        System.out.println(" 等等执行" + deviceInfo);
                        Thread.currentThread().sleep(1000 * 30);
                    }
                    //结束时间往后一小时
                    deviceScheduleService.captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, dateTime.getTime() + 1000 * 60 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (scheduleTask.getTaskType() == 2 || scheduleTask.getTaskType() == 7) {
                String scheduleCycle = null;
                String[] split = scheduleTask.getStartTime().split(":");
                if (scheduleTask.getFrequencyUnit() == 0) {
                    scheduleCycle = String.format("*/%s * * * * ? *", scheduleTask.getFrequency());
                }
                if (scheduleTask.getFrequencyUnit() == 1) {
                    scheduleCycle = String.format("%s */%s * * * ? *", split[2], scheduleTask.getFrequency());
                }
                if (scheduleTask.getFrequencyUnit() == 2) {
                    scheduleCycle = String.format("%s %s */%s * * ? *", split[2], split[1], scheduleTask.getFrequency());
                }
                String endTime = (DateUtil.formatDate(new Date()) + " " + scheduleTask.getEndTime());
                if (DateUtil.parseTime(scheduleTask.getEndTime()).getTime() > DateUtil.parseTime(scheduleTask.getStartTime()).getTime()) {
                    endTime = DateUtil.formatDate(DateUtil.tomorrow()) + " " + scheduleTask.getEndTime();
                }
                DateTime dateTime = DateUtil.parseDateTime(endTime);
                deviceScheduleService.captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, dateTime.getTime());
            }
            //月满计划
            else if (scheduleTask.getTaskType() == 3) {
                String[] days = {"十二", "十三", "十四", "十五", "十六"};
                List<String> list = Arrays.asList(days);
                //判断是否是月满
                ChineseDate chineseDate = new ChineseDate(new Date());
                if (!list.contains(chineseDate.getChineseDay())) {
                    System.out.println("chineseDate = " + chineseDate);
                    log.info(scheduleTask.getScheduleName() + "当前不是满月时间");
                    return;
                }
                startTime = SunRiseSet.getSunrise(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), new Date());
                String[] split = startTime.split(":");
                String scheduleCycle = null;
                try {
                    if (scheduleTask.getFrequencyUnit() == 0) {
                        scheduleCycle = String.format("*/%s * * * * ? *", scheduleTask.getFrequency());
                    }
                    if (scheduleTask.getFrequencyUnit() == 1) {
                        scheduleCycle = String.format("%s */%s * * * ? *", split[2], scheduleTask.getFrequency());
                    }
                    if (scheduleTask.getFrequencyUnit() == 2) {
                        scheduleCycle = String.format("%s %s */%s * * ? *", split[2], split[1], scheduleTask.getFrequency());
                    }
                    startTime = (DateUtil.formatDate(new Date()) + " " + startTime);
                    DateTime dateTime = DateUtil.parseDateTime(startTime);
                    while (new Date().getTime() < dateTime.getTime()) {
                        System.out.println(" 等等执行" + deviceInfo);
                        Thread.currentThread().sleep(1000 * 30);
                    }
                    //获取第二天的日出两调时间
                    String endTime = SunRiseSet.getSunset(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), DateUtil.tomorrow());
                    DateTime time = DateUtil.parseDateTime(DateUtil.tomorrow() + " " + endTime);
                    deviceScheduleService.captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, time.getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}

