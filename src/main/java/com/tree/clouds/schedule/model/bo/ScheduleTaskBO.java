package com.tree.clouds.schedule.model.bo;

import com.tree.clouds.schedule.model.entity.ScheduleTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ScheduleTaskBO extends ScheduleTask {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "设备数量")
    private Integer deviceNumber;

    @ApiModelProperty(value = "执行日期")
    private String date;

    @ApiModelProperty(value = "执行日期")
    private String time;

    @ApiModelProperty(value = "图片数量")
    private Integer imageNumber;

    @ApiModelProperty(value = "图片数量")
    private Integer videoNumber;

}
