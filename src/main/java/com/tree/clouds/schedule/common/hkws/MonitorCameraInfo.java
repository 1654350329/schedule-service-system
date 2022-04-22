package com.tree.clouds.schedule.common.hkws;

import com.sun.jna.NativeLong;
import lombok.Data;

@Data
public class MonitorCameraInfo {
    private String cameraIp;
    private int cameraPort;
    private String userName;
    private String userPwd;
    private NativeLong userId;
    private NativeLong channel;
    private String path;
    private String taskId;
    private short wPicQuality;
    //预置点
    private Integer dwPresetIndex;
    //机器状态
    private Boolean status;
    //通道数
    private Byte channelNumber;
    //错误代码
    private Integer errorCode;
    //错误信息
    private String errorMessage;
    //设备主键
    private String deviceId;

    //创建用户主键
    private String creatUser;

}
