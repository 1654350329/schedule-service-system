package com.tree.clouds.schedule.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceInfoVO {
    @ApiModelProperty(value = "设备id 新增不传 更新传")
    @ExcelIgnore
    private String deviceId;

    @ApiModelProperty(value = "通道名")
    @ExcelProperty("通道名")
    private String deviceName;

    @ApiModelProperty(value = "设备ip地址")
    @ExcelProperty("设备地址")
    private String deviceAddress;

    @ApiModelProperty(value = "设备地址")
    @ExcelProperty("地址")
    private String address;

    @ApiModelProperty(value = "设备型号")
    @ExcelProperty("设备型号")
    private String model;

    @ApiModelProperty(value = "设备类型")
    @ExcelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty(value = "封面图")
    @ExcelProperty("封面图")
    private String image;

    @ApiModelProperty(value = "端口号")
    @ExcelProperty("端口号")
    private Integer port;

    @ApiModelProperty(value = "用户名")
    @ExcelProperty("用户名")
    private String deviceAccount;

    @ApiModelProperty(value = "设备密码")
    @ExcelProperty("密码")
    private String devicePassword;

    @ApiModelProperty(value = "设备经度")
    @ExcelProperty("设备经度")
    private String lng;

    @ApiModelProperty(value = "设备维度")
    @ExcelProperty("设备维度")
    private String lat;

}
