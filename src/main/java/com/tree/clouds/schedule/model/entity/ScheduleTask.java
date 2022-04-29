package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 计划配置
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("schedule_task")
@ApiModel(value = "ScheduleTask对象", description = "计划配置")
public class ScheduleTask extends BaseEntity {

    public static final String SCHEDULE_ID = "schedule_id";
    public static final String SCHEDULE_NAME = "schedule_name";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String CYCLE = "cycle";
    public static final String FREQUENCY = "frequency";
    public static final String CODE_RATE = "code_rate";
    public static final String SCHEDULE_STATUS = "schedule_status";
    public static final Integer STATUS_TRUE = 1;
    public static final Integer STATUS_FALSE = 0;


    @ApiModelProperty(value = "任务id")
    @TableId(value = "schedule_id", type = IdType.UUID)
    private String scheduleId;

    @ApiModelProperty(value = "任务名称")
    @TableField(value = "schedule_name")
    private String scheduleName;

    @ApiModelProperty(value = "任务类型 0 日出 1日落 2自定义 3满月计划 4白天计划 6黑夜计划 7满月计划")
    @TableField("task_type")
    private Integer taskType;

    @ApiModelProperty(value = "每日执行开始时间 格式 时 ", example = "06")
    @TableField("start_time")
    private String startTime;

    @ApiModelProperty(value = "每日执行结束时间 格式 时", example = "08")
    @TableField("end_time")
    private String endTime;

    @ApiModelProperty(value = "执行结束日期", example = "2023-04-01")
    @TableField("end_date")
    private String endDate;

    @ApiModelProperty(value = "执行开始日期", example = "2023-04-01")
    @TableField("start_date")
    private String startDate;

    @ApiModelProperty(value = "归档周期（每天0，每周1，每月2，每年3）")
    @TableField("cycle")
    private Integer cycle;

    @ApiModelProperty(value = "间隔 秒 0-59 分 0-59 时 0-23")
    @TableField("frequency")
    private Integer frequency;

    @ApiModelProperty(value = "间隔单位 0秒 1分 2时")
    @TableField("frequency_unit")
    private Integer frequencyUnit;

    @ApiModelProperty(value = "抽帧码率0超清 1高清 2标清")
    @TableField("code_rate")
    private Short codeRate;
    @ApiModelProperty(value = "任务状态")
    @TableField("schedule_status")
    private Integer scheduleStatus;
    @ApiModelProperty(value = "任务号")
    @TableField("schedule_number")
    private String scheduleNumber;

    @ApiModelProperty(value = "背景音乐id")
    @TableField("music_Id")
    private String musicId;

    @ApiModelProperty(value = "帧率 默认24")
    @TableField("fps")
    private Integer fps;

    @ApiModelProperty(value = "水印类型")
    @TableField("watermark_Type")
    private Integer watermarkType;
    @ApiModelProperty(value = "x坐标")
    @TableField("x")
    private Integer x;
    @ApiModelProperty(value = "y坐标")
    @TableField("y")
    private Integer y;
    @ApiModelProperty(value = "坐标位置 0自定义 1左上角 2左下角 3右上角 4右下角")
    @TableField("coordinate_type")
    private Integer coordinateType;
    @ApiModelProperty(value = "透明度 0.1 1")
    @TableField("alpha")
    private Float alpha;
    @ApiModelProperty(value = "图片路径")
    @TableField("images_Path")
    private String imagesPath;
    @ApiModelProperty(value = "水印文字")
    @TableField("watermark_Text")
    private String watermarkText;
    @ApiModelProperty(value = "字体大小")
    @TableField("font_Size")
    private Integer fontSize;

}
