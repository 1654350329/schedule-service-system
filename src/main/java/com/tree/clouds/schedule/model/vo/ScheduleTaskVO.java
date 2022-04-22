package com.tree.clouds.schedule.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScheduleTaskVO {
    @ApiModelProperty(value = "任务id 新增不传 更新传")
    private String scheduleId;
    @ApiModelProperty(value = "任务名称")
    private String scheduleName;

    @ApiModelProperty(value = "每日执行开始时间 格式 时 分 秒 ", example = "06")
    private String startTime;

    @ApiModelProperty(value = "每日执行结束时间 格式 时", example = "08")
    private String endTime;

    @ApiModelProperty(value = "执行结束日期", example = "2023-04-01")
    private String endDate;

    @ApiModelProperty(value = "执行开始日期", example = "2023-04-01")
    private String startDate;

    @ApiModelProperty(value = "归档周期（每天0，每周1，每月2，每年3）")
    private Integer cycle;

    @ApiModelProperty(value = "间隔 秒 0-59 分 0-59 时 0-23")
    private Integer frequency;

    @ApiModelProperty(value = "间隔单位 0秒 1分 2时")
    private Integer frequencyUnit;

    @ApiModelProperty(value = "抽帧码率0超清 1高清 2标清")
    private Integer codeRate;

    @ApiModelProperty(value = "任务类型 0 日出 1日落 2自定义")
    private Integer taskType;

    @ApiModelProperty(value = "背景音乐id")
    private String musicId;
}
