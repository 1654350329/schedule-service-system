package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 图片列表
 * </p>
 *
 * @author LZK
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("image_info")
@ApiModel(value = "ImageInfo对象", description = "图片列表")
public class ImageInfo extends BaseEntity {
    public static final String IMAGE_ID = "image_id";
    public static final String TASK_ID = "task_id";
    public static final String FILE_PATH = "file_path";
    public static final String PREVIEW_PATH = "preview_path";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";

    @ApiModelProperty(value = "主键")
    @TableId(value = "image_id", type = IdType.UUID)
    private String imageId;

    @ApiModelProperty(value = "任务id")
    @TableField("task_id")
    private String taskId;

    @ApiModelProperty(value = "图片路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty(value = "预览路径")
    @TableField("preview_path")
    private String previewPath;

    @ApiModelProperty(value = "年")
    @TableField("year")
    private String year;

    @ApiModelProperty(value = "月")
    @TableField("month")
    private String month;

    @ApiModelProperty(value = "日")
    @TableField("day")
    private String day;


}
