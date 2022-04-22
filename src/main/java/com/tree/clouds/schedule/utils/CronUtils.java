package com.tree.clouds.schedule.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CronUtils {
    public static String creatTask(Task task, Date startDate, Date endDate, int startTime, int endTime, int frequencyUnit, int frequency) {
        //首次执行任务
        Map<Integer, String> startMap = new HashMap();
        //实际执行任务
        Map<Integer, String> endMap = new HashMap();
        //移除执行任务
        Map<Integer, String> removeMap = new HashMap();
        String[] startDates = DateUtil.formatDate(startDate).split("-");
        DateTime parse = DateUtil.parse(DateUtil.formatDate(startDate) + " " + startTime + ":00:00", "yyyy-MM-dd HH:mm:ss");
        String time;
        if (parse.getTime() <= new Date().getTime()) {
            time = DateUtil.formatTime(new Date(new Date().getTime() + 10000));
            startDates = DateUtil.formatDate(new Date()).split("-");
        } else {
            time = startTime + ":00:00";
        }
        String[] times = time.split(":");
        //首次执行时间
        String schedulingPattern = String.format("%s %s %s %s %s/1 ? %s", times[2], times[1], times[0], startDates[2], startDates[1], startDates[0]);
        System.out.println("schedulingPattern = " + schedulingPattern);
        String schedule = CronUtil.schedule(schedulingPattern, new Task() {
            @Override
            public void execute() {
                String schedule = CronUtil.schedule(schedulingPattern, new Task() {
                    @Override
                    public void execute() {
                        System.out.println("执行到这 ");
                        String schedulingPattern = null;
                        if (frequencyUnit == 0) {
                            schedulingPattern = String.format("0 0 %s-%s/%s * * ? *", startTime, endTime, frequency);
                        }
                        if (frequencyUnit == 1) {
                            schedulingPattern = String.format("0 */%s %s-%s * * ? *", frequency, startTime, endTime);
                        }
                        if (frequencyUnit == 2) {
                            schedulingPattern = String.format("*/%s * %s-%s * * ? *", frequency, startTime, endTime);
                        }
                        CronUtil.schedule(schedulingPattern, task);
                        //移除外层开始任务
                        CronUtil.remove(startMap.get(task.hashCode()));
                    }
                });

                //添加本次任务
                endMap.put(task.hashCode(), schedule);
            }
        });
        startMap.put(task.hashCode(), schedule);
        //结束任务
        String[] endDates = DateUtil.formatDate(endDate).split("-");
        String scheduling = String.format("59 59 %s %s %s/1 ? %s", endTime, endDates[2], endDates[1], endDates[0]);
        String endSchedule = CronUtil.schedule(scheduling, new Task() {
            @Override
            public void execute() {
                //结束所有任务
                CronUtil.remove(endMap.get(task.hashCode()));
                CronUtil.remove(removeMap.get(task.hashCode()));
            }
        });
        removeMap.put(task.hashCode(), endSchedule);

        if (!CronUtil.getScheduler().isStarted()) {
            // 支持秒级别定时任务
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }
        return schedule;

    }

    public static String creatTask(Task task, Date startDate, Date endDate, String y, String x, int type, int cycle, int sunStatus) {
        //首次执行任务时间
        Map<Integer, String> startMap = new HashMap();
        //实际执行任务
        Map<Integer, String> endMap = new HashMap();
        //移除执行任务
        Map<Integer, String> removeMap = new HashMap();
        String[] startDates = DateUtil.formatDate(startDate).split("-");
        //首次执行时间
        String schedulingPattern = String.format("0 47 5 %s/1 %s ? %s", startDates[2], startDates[1], startDates[1]);
        String schedule = CronUtil.schedule(schedulingPattern, new Task() {
            @Override
            public void execute() {
                String schedule = CronUtil.schedule(schedulingPattern, new Task() {
                    @Override
                    public void execute() {
                        String time;
                        if (sunStatus == 0) {
                            time = SunRiseSet.getSunrise(new BigDecimal(y), new BigDecimal(x), new Date());
                        } else {
                            time = SunRiseSet.getSunrise(new BigDecimal(y), new BigDecimal(x), new Date());
                        }
                        String[] times = time.split(":");
                        String schedulingPattern = null;
                        schedulingPattern = String.format("0 %s %s * * ? *", times[1], times[0]);
                        CronUtil.schedule(schedulingPattern, task);
                    }
                });
                //移除外层开始任务
                CronUtil.remove(startMap.get(task.hashCode()));
                //添加本次任务
                endMap.put(task.hashCode(), schedule);
            }
        });
        startMap.put(task.hashCode(), schedule);
        //结束任务
        String[] endDates = DateUtil.formatDate(endDate).split("-");
        String scheduling = String.format("59 59 %s %s %s ? *", "endTime", endDates[2], endDates[1]);
        String endSchedule = CronUtil.schedule(scheduling, new Task() {
            @Override
            public void execute() {
                //结束所有任务
                CronUtil.remove(endMap.get(task.hashCode()));
                CronUtil.remove(removeMap.get(task.hashCode()));
            }
        });
        removeMap.put(task.hashCode(), endSchedule);
        return schedule;

    }

    public static void main(String[] args) {
        ImgUtil.scale(
                FileUtil.file("D:\\image\\f7fdd17e2b1b293bfef38f3965eae8d9\\2022\\04\\06\\20220406175200.jpg"),
                FileUtil.file("D:\\461.jpg"),
                0.15f//缩放比例
        );
    }
}
