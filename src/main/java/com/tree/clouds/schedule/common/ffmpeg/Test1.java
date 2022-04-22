package com.tree.clouds.schedule.common.ffmpeg;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tree.clouds.schedule.common.Constants;
import org.junit.Test;
import org.quartz.DailyTimeIntervalScheduleBuilder;
import org.quartz.TimeOfDay;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Test1 {

    public static StringBuilder doWaitFor(Process p) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1;

        try {
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false;

            while (!finished) {
                try {
                    Character c;
                    while (in.available() > 0) {
                        c = new Character((char) in.read());
                        stringBuilder.append(c);
                        System.out.print(c);
                    }

                    while (err.available() > 0) {
                        c = new Character((char) err.read());
                        stringBuilder.append(c);
                        System.out.print(c);
                    }

                    exitValue = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException var19) {
                    Thread.currentThread();
                    Thread.sleep(500L);
                }
            }
        } catch (Exception var20) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException var18) {
            }

            if (err != null) {
                try {
                    err.close();
                } catch (IOException var17) {
                }
            }

        }

        return stringBuilder;
    }

    /**
     * 把图片拼接成视频,这里取20张图片,生成10秒的视频
     */
    @Test
    public void test3() {
        String musicPath = "D:\\台湾来的小阿霞.mp3";
        long startTime = System.currentTimeMillis();
//
        String outPath = "D:\\k12k.mp4";
        String sourcePath = "D:\\MP4";
//            List<String> commend = new ArrayList();
//            //以1fps读入
//            commend.add( Constants.ffmpeg_path);
//            commend.add("-r");
//            commend.add("24");
//            //-threads 2 以两个线程进行运行， 加快处理的速度。
//            commend.add("-threads");
//            commend.add("2");
//            commend.add("-f");
//            commend.add("image2");
//            commend.add("-i");
//            commend.add(sourcePath);
//            commend.add("-vcodec");
//            commend.add("libx264");
//            //-y 对输出文件进行覆盖
//            commend.add("-y");
//            //-r 5 fps设置为5帧/秒（不同位置有不同含义)  %2d标识图片文件的正则
//            commend.add("-r");
//            commend.add("5");
//            commend.add(outPath);
////            //添加背景音乐
////            //获取视频时长
//            start(commend);
//            Info info = new Info();
//            String size = info.size(outPath);
//
//            String[] split = size.replace(".", ":").split(":");
//            int time = Integer.parseInt(split[0]) * 60 * 60 + Integer.parseInt(split[1]) * 60 + Integer.parseInt(split[2]);
//            if (time == 0) {
//                time = 1;
//            }
        Double time = FileUtil.ls(sourcePath).length / Double.parseDouble("24");
        System.out.println("time = " + time);
//            List<String> list = new ArrayList<>();
//            list.add( Constants.ffmpeg_path);
//            list.add("-i");
//            list.add(sourcePath);
//            list.add("-stream_loop");
//            list.add("-1");
//            list.add("-i");
//            list.add(musicPath);
//            list.add("-ss");
//            list.add("0");
//            list.add("-t");
//            list.add(time + "");
//            list.add("-y");
//            list.add(outPath);
//            start(list);
//


        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }

    private void start(List<String> commend) {
        Process p = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(commend);
            builder.command(commend);
            p = builder.start();
            p.getOutputStream().close();
            doWaitFor(p);
            p.destroy();
        } catch (Exception e) {
            p.destroy();
        }
    }
}