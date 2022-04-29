package com.tree.clouds.schedule.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.DeviceInfoService;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.service.ScheduleTaskService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TaskSchedule implements Task {

    private ScheduleTask scheduleTask;
    private DeviceScheduleService deviceScheduleService;
    private DeviceInfoService deviceInfoService;
    private ScheduleTaskService scheduleTaskService;

    public TaskSchedule(ScheduleTask scheduleTask, ScheduleTaskService scheduleTaskService, DeviceScheduleService deviceScheduleService, DeviceInfoService deviceInfoService) {
        this.scheduleTask = scheduleTask;
        this.scheduleTaskService = scheduleTaskService;
        this.deviceScheduleService = deviceScheduleService;
        this.deviceInfoService = deviceInfoService;
    }

    @Override
    public void execute() {
        //开启归档任务
        deviceScheduleService.archiveTask(scheduleTask);
        String dateTime = scheduleTask.getEndDate() + " 23:59:59";
        if (new Date().getTime() > DateUtil.parseDateTime(dateTime).getTime()) {
            return;
        }
        String schedule = null;
        //日出
        if (scheduleTask.getTaskType() == 0) {
            //执行时间
            String date = DateUtil.formatDate(new Date()) + " 04:20:00";
            DateTime parseDate = DateUtil.parseDateTime(date);

            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
            }
            String[] dates = date.replace(" ", "-").replace(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s */1 * ? *", dates[5], dates[4], dates[3]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("日出计划schedulingPattern = " + schedulingPattern);
        }
        //日落
        if (scheduleTask.getTaskType() == 1) {
            //执行时间
            String date = DateUtil.formatDate(new Date()) + " 16:28:00";
            DateTime parseDate = DateUtil.parseDateTime(date);
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 10));
            }
            String[] dates = date.replace(" ", "-").replace(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s */1 * ? *", dates[5], dates[4], dates[3]);

            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("日落计划schedulingPattern = " + schedulingPattern);
        }
        //自定义时间与跨日计划
        if (scheduleTask.getTaskType() == 2 || scheduleTask.getTaskType() == 7) {
            //执行时间
            String date = DateUtil.formatDate(new Date()) + " " + scheduleTask.getStartTime();
            DateTime parseDate = DateUtil.parseDateTime(date);
            date = DateUtil.formatDateTime(new Date(parseDate.getTime() - 1000 * 30));
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
            }
            String[] dates = date.replace(" ", "-").replaceAll(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s */1 * ? *", dates[5], dates[4], dates[3]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("自定义计划schedulingPattern = " + schedulingPattern);
        }
        //满月计划与黑夜计划
        if (scheduleTask.getTaskType() == 3 || scheduleTask.getTaskType() == 6) {
            //执行时间
            String date = DateUtil.formatDate(new Date()) + " 16:28:00";
            DateTime parseDate = DateUtil.parseDateTime(date);
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
            }
            String[] dates = date.replace(" ", "-").replaceAll(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s */1 * ? *", dates[5], dates[4], dates[3]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("黑夜或满月计划schedulingPattern = " + schedulingPattern);
        }
        //白天计划
        if (scheduleTask.getTaskType() == 4) {
            //执行时间
            String date = DateUtil.formatDate(new Date()) + " 04:28:00";
            DateTime parseDate = DateUtil.parseDateTime(date);
            if (parseDate.getTime() <= new Date().getTime()) {
                date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
            }
            String[] dates = date.replace(" ", "-").replaceAll(":", "-").split("-");
            String schedulingPattern = String.format("%s %s %s */1 * ? *", dates[5], dates[4], dates[3]);
            SumRiseSetTask sumRiseSetTask = new SumRiseSetTask(scheduleTask, deviceScheduleService, deviceInfoService);
            schedule = CronUtil.schedule(schedulingPattern, sumRiseSetTask);
            log.info("白天月计划schedulingPattern = " + schedulingPattern);
        }
        //更新状态
        ScheduleTask task = new ScheduleTask();
        task.setScheduleId(scheduleTask.getScheduleId());
        task.setScheduleNumber(schedule);
        task.setScheduleStatus(ScheduleTask.STATUS_TRUE);
        this.scheduleTaskService.updateById(task);
        CronUtil.remove(Constants.tempMap.get(scheduleTask.getScheduleId()));
    }
}
