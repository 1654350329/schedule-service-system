package com.tree.clouds.schedule.task;

import cn.hutool.cron.task.Task;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.DeviceScheduleService;

public class TaskSchedule implements Task {
    private String schedulingPattern;
    private ScheduleTask scheduleTask;
    private DeviceScheduleService deviceScheduleService;

    public TaskSchedule(String schedulingPattern, ScheduleTask scheduleTask, DeviceScheduleService deviceScheduleService) {
        this.schedulingPattern = schedulingPattern;
        this.scheduleTask = scheduleTask;
        this.deviceScheduleService = deviceScheduleService;
    }

    @Override
    public void execute() {
        deviceScheduleService.startSchedule(scheduleTask, schedulingPattern);
    }
}
