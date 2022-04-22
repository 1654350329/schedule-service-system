package com.tree.clouds.schedule.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScheduleTaskPageVO extends PageParam {
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

    @ApiModelProperty(value = "设备id")
    private String deviceId;
    @ApiModelProperty(value = "任务状态")
    private Integer scheduleStatus;

}
