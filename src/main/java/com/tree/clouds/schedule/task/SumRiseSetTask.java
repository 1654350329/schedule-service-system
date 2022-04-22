package com.tree.clouds.schedule.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.task.Task;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.DeviceInfoService;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.utils.SunRiseSet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        //开启归档任务
        deviceScheduleService.archiveTask(scheduleTask);
        List<DeviceSchedule> deviceSchedules = deviceScheduleService.getByScheduleId(scheduleTask.getScheduleId());
        for (DeviceSchedule deviceSchedule : deviceSchedules) {
            DeviceInfo deviceInfo = deviceInfoService.getById(deviceSchedule.getDeviceId());
            String startTime = null;
            if (scheduleTask.getTaskType() == 0) {
                startTime = SunRiseSet.getSunrise(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), new Date());
            }
            if (scheduleTask.getTaskType() == 1) {
                startTime = SunRiseSet.getSunset(new BigDecimal(deviceInfo.getLng()), new BigDecimal(deviceInfo.getLat()), new Date());
            }
            DateTime parse = DateUtil.parse(startTime, "HH:mm:ss");
            //日出落前半小时
            startTime = DateUtil.format(new Date(parse.getTime() - (1000 * 60 * 30)), "HH:mm:ss");
            startTime = (DateUtil.formatDate(new Date()) + " " + startTime);
            DateTime dateTime = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
            try {
                String scheduleCycle = String.format("*/%s * * * * ? *", scheduleTask.getFrequency());
                while (new Date().getTime() < dateTime.getTime()) {
                    System.out.println(" 等等执行" + deviceInfo);
                    Thread.currentThread().sleep(1000 * 30);
                }
                deviceScheduleService.captureTask(deviceSchedule, scheduleTask.getCodeRate(), scheduleCycle, 0, dateTime.getTime());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
