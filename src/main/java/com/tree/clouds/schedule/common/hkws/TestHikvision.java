package com.tree.clouds.schedule.common.hkws;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.model.entity.ImageInfo;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.DeviceLogService;
import com.tree.clouds.schedule.service.ImageInfoService;
import com.tree.clouds.schedule.utils.ImgInfoUtil;
import com.tree.clouds.schedule.utils.SystemUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.tree.clouds.schedule.init.JDDRunner.sdk;

@Slf4j
public class TestHikvision implements Task {
    private static final Map<String, NativeLong> hcNetSDKMap = new HashMap<>();
    //记录设备状态
    private static final Map<String, Boolean> deviceMap = new ConcurrentHashMap<>();
    private MonitorCameraInfo cameraInfo;
    private Long endTime;
    private DeviceLogService deviceLogService;
    private ImageInfoService imageInfoService;
    private ScheduleTask scheduleTask;

    public TestHikvision(MonitorCameraInfo cameraInfo, ScheduleTask scheduleTask, Long endTime, DeviceLogService deviceLogService, ImageInfoService imageInfoService) {
        this.cameraInfo = cameraInfo;
        this.scheduleTask = scheduleTask;
        this.endTime = endTime;
        this.deviceLogService = deviceLogService;
        this.imageInfoService = imageInfoService;
    }

    //抓拍图片
    public static void getDVRPic(MonitorCameraInfo cameraInfo, ScheduleTask scheduleTask, DeviceLogService deviceLogService, ImageInfoService imageInfoService) {

        try {
            long startTime = System.currentTimeMillis();
            log.info("本次执行 = " + scheduleTask.getScheduleName() + "设备号:" + cameraInfo.getDeviceId() + "任务号:" + cameraInfo.getTaskId());
            if (!hcNetSDKMap.containsKey(cameraInfo.getDeviceId())) {
                HCNetSDK.NET_DVR_DEVICEINFO_V30 devinfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备信息
                //注册设备
                NativeLong id = sdk.NET_DVR_Login_V30(cameraInfo.getCameraIp(), (short) cameraInfo.getCameraPort(),
                        cameraInfo.getUserName(), cameraInfo.getUserPwd(), devinfo);
                cameraInfo.setUserId(id);// 返回一个用户编号，同时将设备信息写入devinfo
                if (cameraInfo.getUserId().intValue() < 0) {
                    log.warn("设备注册失败" + sdk.NET_DVR_GetLastError() + "***" + cameraInfo);
                    deviceMap.put(cameraInfo.getDeviceId(), false);
                    deviceLogService.saveLog(cameraInfo.getDeviceId(), sdk.NET_DVR_GetLastError(), false);
                    return;
                } else {
                    System.out.println("id：" + cameraInfo.getUserId().intValue());
                    hcNetSDKMap.put(cameraInfo.getDeviceId(), id);
                }
                System.out.println("设备注册耗时：[" + (System.currentTimeMillis() - startTime) + "]");
                log.info("设备注册耗时：[" + (System.currentTimeMillis() - startTime) + "]");
            }

            cameraInfo.setUserId(hcNetSDKMap.get(cameraInfo.getDeviceId()));
            if (cameraInfo.getDwPresetIndex() != null && cameraInfo.getDwPresetIndex() != 0) {
                //跳转到预置点
                HCNetSDK.NET_DVR_CLIENTINFO clientinfo = new HCNetSDK.NET_DVR_CLIENTINFO();
                clientinfo.lChannel = cameraInfo.getChannel();
                clientinfo.hPlayWnd = null;

                NativeLong nativeLong = sdk.NET_DVR_RealPlay(cameraInfo.getUserId(),
                        clientinfo);
                sdk.NET_DVR_PTZPreset(nativeLong, HCNetSDK.GOTO_PRESET, cameraInfo.getDwPresetIndex());
                sdk.NET_DVR_StopRealPlay(nativeLong);
            }
            startTime = System.currentTimeMillis();
            //图片质量
            HCNetSDK.NET_DVR_JPEGPARA jpeg = new HCNetSDK.NET_DVR_JPEGPARA();
            // 设置图片的分辨率
            jpeg.wPicSize = 2;
            // 设置图片质量
            jpeg.wPicQuality = cameraInfo.getWPicQuality();

            IntByReference byReference = new IntByReference();
            //设置图片大小
            ByteBuffer jpegBuffer = ByteBuffer.allocate(1024 * 1024 * 5);
            // 创建文件目录和文件
            //按照每天设置一个目录
            //按任务与年月日建目录
            String[] split = DateUtil.formatDate(new Date()).split("-");
            String filePath = Constants.SCHEDULE_PATH + cameraInfo.getTaskId() + File.separator + split[0] + File.separator + split[1] + File.separator + split[2];
            FileUtil.mkdir(filePath);
            //按照日期文件夹放入图片
            String format = DateUtil.format(new Date(), "YYYYMMddHHmmss");
            String fileName = filePath + "/" + format + ".jpg";
            File file = new File(fileName);
//             抓图到内存，单帧数据捕获并保存成JPEG存放在指定的内存空间中
//            需要加入通道
            String dateTime;
            if (SystemUtil.getSystemByName().equalsIgnoreCase("Linux")) {
                dateTime = DateUtil.formatDateTime(new Date());
            } else {
                dateTime = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 10));
            }
            boolean is = sdk.NET_DVR_CaptureJPEGPicture_NEW(cameraInfo.getUserId(), cameraInfo.getChannel(), jpeg,
                    jpegBuffer, 1024 * 1024 * 5, byReference);
            System.out.println("抓图到内存耗时：[" + (System.currentTimeMillis() - startTime) + "ms]" + filePath);
            while (!is && (sdk.NET_DVR_GetLastError() == 9 || sdk.NET_DVR_GetLastError() == 10)) {
                is = sdk.NET_DVR_CaptureJPEGPicture_NEW(cameraInfo.getUserId(), cameraInfo.getChannel(), jpeg,
                        jpegBuffer, 1024 * 1024 * 5, byReference);
            }

            if (!is) {
                cameraInfo.setStatus(false);
                cameraInfo.setErrorCode(sdk.NET_DVR_GetLastError());
                cameraInfo.setErrorMessage("抓取失败");
                log.warn("抓取失败：" + sdk.NET_DVR_GetLastError() + "****" + cameraInfo);
                deviceMap.put(cameraInfo.getDeviceId(), false);
                deviceLogService.saveLog(cameraInfo.getDeviceId(), sdk.NET_DVR_GetLastError(), false);
                return;
            }

            // 存储本地，写入内容
            makeFile(byReference, jpegBuffer, file);
            //添加文字水印
            if (scheduleTask.getWatermarkType() != null && scheduleTask.getWatermarkType() != 0) {
                int x;
                int y;
                if (scheduleTask.getCoordinateType() == 0) {
                    x = scheduleTask.getX();
                    y = scheduleTask.getY();
                } else {
                    Map<String, Integer> topLeftInfo = ImgInfoUtil.getInfo(file, scheduleTask.getCoordinateType());
                    x = topLeftInfo.get("x");
                    y = topLeftInfo.get("y");
                }
                if (scheduleTask.getWatermarkType() == 1) {
                    ImgUtil.pressText(//
                            FileUtil.file(file.getAbsolutePath()), //
                            FileUtil.file(file.getAbsolutePath()), //
                            scheduleTask.getWatermarkText(), Color.RED, //文字
                            new Font("黑体", Font.BOLD, 100), //字体
                            x, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            y, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            scheduleTask.getAlpha()//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );
                } else {
                    //添加图片水印
                    ImgUtil.pressImage(
                            FileUtil.file(file.getAbsolutePath()),
                            FileUtil.file(file.getAbsolutePath()),
                            ImgUtil.read(FileUtil.file(Constants.Root_PATH + scheduleTask.getImagesPath())), //水印图片
                            scheduleTask.getX(), //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                            scheduleTask.getY(), //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                            scheduleTask.getAlpha()//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
                    );
                }
            }
            //生成预览图
            File preFile = new File(Constants.PREVIEW_PATH + UUID.randomUUID() + file.getName());
            ImgUtil.scale(
                    FileUtil.file(file.getAbsolutePath()),
                    FileUtil.file(preFile),
                    0.15f//缩放比例
            );
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setTaskId(cameraInfo.getTaskId());
            imageInfo.setFilePath(fileName.replace(Constants.Root_PATH, ""));
            imageInfo.setPreviewPath(preFile.getPath().replace(Constants.Root_PATH, ""));
            imageInfo.setYear(split[0]);
            imageInfo.setMonth(split[1]);
            imageInfo.setDay(split[2]);
            imageInfo.setCreatedTime(dateTime);
            imageInfo.setCreatedUser(cameraInfo.getCreatUser());
            imageInfo.setUpdatedUser(cameraInfo.getCreatUser());
            imageInfoService.save(imageInfo);
            //第一次启动 记录
            if (!deviceMap.containsKey(cameraInfo.getDeviceId())) {
                deviceLogService.saveLog(cameraInfo.getDeviceId(), null, true);
            }
            //只记录失败后第一次恢复日志
            if (deviceMap.containsKey(cameraInfo.getDeviceId()) && !deviceMap.get(cameraInfo.getDeviceId())) {
                deviceLogService.saveLog(cameraInfo.getDeviceId(), null, true);
            }
            deviceMap.put(cameraInfo.getDeviceId(), true);
//            sdk.NET_DVR_Logout(cameraInfo.getUserId());
//            sdk.NET_DVR_Cleanup();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        if (endTime != null && new Date().getTime() > endTime) {
            log.info("本次任务已结束:" + scheduleTask.getScheduleName());
            CronUtil.remove(Constants.scheduleMap.get(cameraInfo.getTaskId()));
            Constants.scheduleMap.remove(cameraInfo.getTaskId());
            return;
        }
        getDVRPic(cameraInfo, scheduleTask, deviceLogService, imageInfoService);

    }


    private static void makeFile(IntByReference byReference, ByteBuffer jpegBuffer, File file) {
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(jpegBuffer.array(), 0, byReference.getValue());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static MonitorCameraInfo getDVRConfig(MonitorCameraInfo cameraInfo) {
        HCNetSDK sdk = HCNetSDK.INSTANCE;
        //判断摄像头是否开启
        if (!sdk.NET_DVR_Init()) {
            System.out.println("SDK初始化失败");
            NativeLong nativeLong = new NativeLong();
            nativeLong.setValue(-1);
            cameraInfo.setUserId(nativeLong);
            return cameraInfo;
        }
        //设置连接时间与重连
        sdk.NET_DVR_SetConnectTime(2000, 1);
        sdk.NET_DVR_SetReconnect(10000, true);

        HCNetSDK.NET_DVR_DEVICEINFO_V30 devinfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备信息

        //System.out.println("设备信息："+devinfo);

        //登录信息
        NativeLong id = sdk.NET_DVR_Login_V30(cameraInfo.getCameraIp(), (short) cameraInfo.getCameraPort(),
                cameraInfo.getUserName(), cameraInfo.getUserPwd(), devinfo);

        cameraInfo.setUserId(id);// 返回一个用户编号，同时将设备信息写入devinfo
        //输出int数据
        //System.out.println("intValue:"+cameraInfo.getUserId().intValue());
        if (cameraInfo.getUserId().intValue() < 0) {
            System.out.println("设备注册失败" + sdk.NET_DVR_GetLastError());
            NativeLong nativeLong = new NativeLong();
            nativeLong.setValue(-1);
            cameraInfo.setUserId(nativeLong);
            cameraInfo.setStatus(false);
            cameraInfo.setErrorMessage("设备注册失败");
            cameraInfo.setErrorCode(sdk.NET_DVR_GetLastError());
            return cameraInfo;
        }
        //DVR工作状态
        HCNetSDK.NET_DVR_WORKSTATE_V30 devwork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
        if (!sdk.NET_DVR_GetDVRWorkState_V30(cameraInfo.getUserId(), devwork)) {
            // 返回Boolean值，判断是否获取设备能力
            System.out.println("返回设备状态失败" + sdk.NET_DVR_GetLastError());
            NativeLong nativeLong = new NativeLong();
            nativeLong.setValue(-1);
            cameraInfo.setUserId(nativeLong);
            cameraInfo.setStatus(false);
            cameraInfo.setErrorMessage("返回设备状态失败");
            cameraInfo.setErrorCode(sdk.NET_DVR_GetLastError());
            return cameraInfo;
        }

        IntByReference ibrBytesReturned = new IntByReference(0);// 获取IP接入配置参数
        HCNetSDK.NET_DVR_IPPARACFG ipcfg = new HCNetSDK.NET_DVR_IPPARACFG();//IP接入配置结构
        ipcfg.write();
        Pointer lpIpParaConfig = ipcfg.getPointer();
        //获取相关参数配置
        sdk.NET_DVR_GetDVRConfig(cameraInfo.getUserId(), HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0),
                lpIpParaConfig, ipcfg.size(), ibrBytesReturned);
        ipcfg.read();
        System.out.print("IP地址:" + cameraInfo.getCameraIp());
        log.info("IP地址:" + cameraInfo.getCameraIp());
        log.info("|设备状态：" + devwork.dwDeviceStatic + "****0正常，1CPU占用率过高，2硬件错误，3未知");
        System.out.println("|设备状态：" + devwork.dwDeviceStatic);// 0正常，1CPU占用率过高，2硬件错误，3未知
        //System.out.println("ChanNum"+devinfo.byChanNum);
        // 显示模拟通道
        for (int i = 0; i < devinfo.byChanNum; i++) {
            System.out.print("Camera" + i + 1);// 模拟通道号名称
            System.out.print("|是否录像:" + devwork.struChanStatic[i].byRecordStatic);// 0不录像，不录像
            System.out.print("|信号状态:" + devwork.struChanStatic[i].bySignalStatic);// 0正常，1信号丢失
            System.out.println("|硬件状态:" + devwork.struChanStatic[i].byHardwareStatic);// 0正常，1异常
            log.info("|是否录像:" + devwork.struChanStatic[i].byRecordStatic);
            log.info("|信号状态:" + devwork.struChanStatic[i].bySignalStatic);
            log.info("|硬件状态:" + devwork.struChanStatic[i].byHardwareStatic);
        }
        cameraInfo.setChannelNumber(devinfo.byChanNum);
        cameraInfo.setStatus(true);
        //注销用户
        sdk.NET_DVR_Logout(cameraInfo.getUserId());//释放SDK资源
        sdk.NET_DVR_Cleanup();
        return cameraInfo;
    }


}