package com.tree.clouds.schedule.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Constants {
    public static final String TMP_HOME = "/temp/";
    //存放抓拍任务id
    public static final Map<String, String> scheduleMap = new ConcurrentHashMap<>();
    //存放归档任务id
    public static final Map<String, String> taskMap = new ConcurrentHashMap<>();
    public static final Map<Integer, String> errorMap = new LinkedHashMap<>();
    public static String Root_PATH = "E:\\schedule";
    //抓拍图片存放路径
    public static final String SCHEDULE_PATH = Root_PATH + "\\image\\";
    public static final String MP4_PATH = Root_PATH + "\\MP4\\";
    public static final String MUSIC_PATH = Root_PATH + "\\music\\";
    public static final String HLS = Root_PATH + "\\hls\\";
    public static final String ffprobe_path = Root_PATH + "\\ffprobe.exe ";

    public static String STATIC_PATH = "E://schedule/image/";
    public static String ffmpeg_path = Root_PATH + "\\ffmpeg.exe ";
    public static String HCNETSDK = "HCNETSDK";
    //预览路径
    public static String PREVIEW_PATH = SCHEDULE_PATH + "preview\\";

    //linux版本

//    public static String STATIC_PATH = "/mydata/image/";
//    public static String Root_PATH = "/mydata";
//    //抓拍图片存放路径
//    public static final String SCHEDULE_PATH = Root_PATH + "/image/";
//    public static final String MP4_PATH = Root_PATH + "/MP4/";
//    public static final String MUSIC_PATH = Root_PATH + "/music/";
//    public static final String HLS = Root_PATH + "/hls/";
//    public static String ffmpeg_path = "ffmpeg";
//    public static String HCNETSDK = "/jar/libhcnetsdk.so";
//    //预览路径
//    public static String PREVIEW_PATH = SCHEDULE_PATH + "preview/";


}
