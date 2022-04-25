package com.tree.clouds.schedule.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.common.hkws.windows.HCNetSDK;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.service.ScheduleTaskService;
import com.tree.clouds.schedule.utils.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JDDRunner implements ApplicationRunner {
    public static HCNetSDK sdk = HCNetSDK.INSTANCE;
    @Autowired
    private ScheduleTaskService scheduleTaskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (SystemUtil.getSystemByName().equalsIgnoreCase("Linux")) {
            //linux版本
            Constants.STATIC_PATH = "/mydata/image/";
            Constants.Root_PATH = "/mydata";
            //抓拍图片存放路径
            Constants.SCHEDULE_PATH = Constants.Root_PATH + "/image/";
            Constants.MP4_PATH = Constants.Root_PATH + "/MP4/";
            Constants.MUSIC_PATH = Constants.Root_PATH + "/music/";
            Constants.HLS = Constants.Root_PATH + "/hls/";
            Constants.ffmpeg_path = "ffmpeg";
            Constants.HCNETSDK = "/jar/libhcnetsdk.so";
            //预览路径
            Constants.PREVIEW_PATH = Constants.SCHEDULE_PATH + "preview/";
        }
        //海康威视名称
        if (!sdk.NET_DVR_Init()) {
            System.out.println("SDK初始化失败");
            log.error("SDK初始化失败");
        }
        //设置连接时间与重连
        sdk.NET_DVR_SetConnectTime(2000, 1);
        sdk.NET_DVR_SetReconnect(10000, true);

        if (!CronUtil.getScheduler().isStarted()) {
            // 支持秒级别定时任务
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }
        List<ScheduleTask> list = scheduleTaskService.list(new QueryWrapper<ScheduleTask>().eq(ScheduleTask.SCHEDULE_STATUS, ScheduleTask.STATUS_TRUE));
        for (ScheduleTask scheduleTask : list) {
            scheduleTaskService.startSchedule(scheduleTask.getScheduleId());
            System.out.println("开启定时任务计划");
        }
        //添加设备失败字典
        buildErrorMap();
        //创建文件夹
        FileUtil.mkdir(Constants.MP4_PATH);
        FileUtil.mkdir(Constants.MUSIC_PATH);
        FileUtil.mkdir(Constants.PREVIEW_PATH);
        FileUtil.mkdir(Constants.SCHEDULE_PATH);
        FileUtil.mkdir(Constants.HLS);

    }

    public void buildErrorMap() {
        Constants.errorMap.put(7, "连接设备失败。设备不在线或网络原因引起的连接超时等");
        Constants.errorMap.put(8, "向设备发送失败");
        Constants.errorMap.put(9, "从设备接收数据失败");
        Constants.errorMap.put(10, "从设备接收数据超时");
        Constants.errorMap.put(21, "设备硬盘满");
        Constants.errorMap.put(29, "设备操作失败");
    }

}