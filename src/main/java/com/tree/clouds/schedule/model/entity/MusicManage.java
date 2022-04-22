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
 * 音乐表
 * </p>
 *
 * @author LZK
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("music_manage")
@ApiModel(value = "MusicManage对象", description = "音乐表")
public class MusicManage extends BaseEntity {
    public static final String MUSIC_ID = "music_id";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_TYPE = "file_type";
    public static final String FILE_SIZE = "file_size";
    public static final String FILE_PATH = "file_path";

    @ApiModelProperty(value = "音乐id")
    @TableId(value = "music_id", type = IdType.UUID)
    private String musicId;

    @ApiModelProperty(value = "文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "文件格式")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    @TableField("file_size")
    private String fileSize;

    @ApiModelProperty(value = "存放路径")
    @TableField("file_path")
    private String filePath;


}
