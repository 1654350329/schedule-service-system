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
 * 设备信息
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("device_info")
@ApiModel(value = "DeviceInfo对象", description = "设备信息")
public class DeviceInfo extends BaseEntity {
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String CHANNEL = "channel";
    public static final String PORT = "port";
    public static final String DEVICE_ACCOUNT = "device_account";
    public static final String DEVICE_PASSWORD = "device_password";
    public static final String DEVICE_STATUS = "device_status";


    @ApiModelProperty(value = "设备id")
    @TableId(value = "device_id", type = IdType.UUID)
    private String deviceId;

    @ApiModelProperty(value = "通道名")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value = "设备ip地址")
    @TableField("device_address")
    private String deviceAddress;

    @ApiModelProperty(value = "设备地址")
    @TableField("address")
    private String address;

    @ApiModelProperty(value = "通道数")
    @TableField("channel_number")
    private Byte channelNumber;

    @ApiModelProperty(value = "设备型号")
    @TableField("model")
    private String model;

    @ApiModelProperty(value = "设备类型")
    @TableField("device_type")
    private Integer deviceType;

    @ApiModelProperty(value = "封面图")
    @TableField("image")
    private String image;

    @ApiModelProperty(value = "端口号")
    @TableField("port")
    private Integer port;

    @ApiModelProperty(value = "用户名")
    @TableField("device_account")
    private String deviceAccount;

    @ApiModelProperty(value = "设备密码")
    @TableField("device_password")
    private String devicePassword;

    @ApiModelProperty(value = "设备经度")
    @TableField("lng")
    private String lng;

    @ApiModelProperty(value = "设备维度")
    @TableField("lat")
    private String lat;

    @ApiModelProperty(value = "设备状态")
    @TableField("device_status")
    private Integer deviceStatus;

    @ApiModelProperty(value = "图片数量")
    @TableField(exist = false)
    private Integer imageNumber;

    @ApiModelProperty(value = "图片数量")
    @TableField(exist = false)
    private Integer videoNumber;


}
