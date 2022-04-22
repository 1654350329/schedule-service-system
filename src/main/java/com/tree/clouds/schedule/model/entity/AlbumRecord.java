package com.tree.clouds.schedule.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 相册记录
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("album_record")
@ApiModel(value = "AlbumRecord对象", description = "相册记录")
public class AlbumRecord extends BaseEntity {
    public static final String RECORD_ID = "record_id";

    public static final String FILE_NAME = "file_name";

    public static final String FILE_SIZE = "file_size";

    public static final String DURATION = "duration";

    public static final String FILE_PATH = "file_path";

    public static final String FILE_ADDRESS = "file_address";
    public static final String TASK_ID = "task_id";

    @ApiModelProperty(value = "记录id")
    @TableId(value = "record_id", type = IdType.UUID)
    private String recordId;

    @ApiModelProperty(value = "任务id")
    @TableField(value = "task_id")
    private String taskId;

    @ApiModelProperty(value = "文件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "记录视频开始结束日期")
    @TableField("play_period")
    private String playPeriod;

    @ApiModelProperty(value = "文件大小")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty(value = "视频时长")
    @TableField("duration")
    private String duration;

    @ApiModelProperty(value = "文件地址")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty(value = "点播流地址")
    @TableField("file_address")
    private String fileAddress;

    @ApiModelProperty(value = "预览封面地址")
    @TableField("preview_image")
    private String previewImage;


}
