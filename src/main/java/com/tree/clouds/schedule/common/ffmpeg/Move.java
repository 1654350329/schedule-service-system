package com.tree.clouds.schedule.common.ffmpeg;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.tree.clouds.schedule.service.AlbumRecordService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Move extends Base implements Task {

    private AlbumRecordService albumRecordService;
    private Integer cycle;
    private String taskId;
    private String musicPath;
    private int fps;
    private int type;
    private DateTime endTime;
    private String creatUser;

    public Move(String creatUser, Integer type, Integer cycle, DateTime endTime, String taskId, String musicPath, int fps, AlbumRecordService albumRecordService) {
        this.type = type;
        this.creatUser = creatUser;
        this.cycle = cycle;
        this.endTime = endTime;
        this.taskId = taskId;
        this.fps = fps;
        this.musicPath = musicPath;
        this.albumRecordService = albumRecordService;
    }

    public static synchronized void buildM3u8(String sourcePath, String outPath) {
        long startTime = System.currentTimeMillis();
        //添加背景音乐
        if (StrUtil.isNotBlank(sourcePath)) {
            if (!FileUtil.exist(sourcePath)) {
                log.info("文件不存在:" + sourcePath);
                return;
            }
        }
        List<String> commend = new ArrayList();
        //以1fps读入
        commend.add(Constants.ffmpeg_path);
        commend.add("-i");
        commend.add(sourcePath);
        //-threads 2 以两个线程进行运行， 加快处理的速度。
        commend.add("-threads");
        commend.add("2");
        commend.add("-hls_time");
        commend.add("5");
        commend.add("-hls_list_size");
        commend.add("0");
        commend.add("-hls_segment_filename");
        commend.add(outPath + "_%05d.ts");
        commend.add(outPath + ".m3u8");
        start(commend);
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }

    public static synchronized void executeCmd(String sourcePath, Integer fps, String outPath, String musicPath) {
        System.out.println("开始执行生成视频" + outPath);
        long startTime = System.currentTimeMillis();
        if (fps == 0) {
            fps = 24;
        }
        double time = FileUtil.ls(sourcePath).length / Double.parseDouble(String.valueOf(fps));
        List<String> list = new ArrayList<>();
        list.add(Constants.ffmpeg_path);
        list.add("-i");
        list.add(sourcePath + "%d.jpg");
        list.add("-stream_loop");
        list.add("-1");
        list.add("-i");
        list.add(musicPath);
        list.add("-ss");
        list.add("0");
        list.add("-t");
        list.add(time + "");
        list.add("-vcodec");
        list.add("libx264");
        list.add("-y");
        list.add(outPath);
        start(list);


        long endTime = System.currentTimeMillis();
        System.out.println("结束执行生成视频" + outPath);
        System.out.println((endTime - startTime));
    }

    /***
     * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
     *
     * @param obj
     * @return
     */
    public static ArrayList<File> getListFiles(Object obj) {
        File directory = null;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }

    public static File[] orderByDate(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return -1;
                else if (diff == 0)
                    return 0;
                else
                    return 1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减,如果 if 中修改为 返回1 同时此处修改为返回 -1  排序就会是递增,
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return files;

    }


    /***
     * 获取指定目录下的所有的文件（不包括文件夹），采用了递归
     *
     * @param filePaths
     * @return
     */
    public static List<File> getListFiles(List<String> filePaths) {
        ArrayList<File> files = new ArrayList<>();
        for (String filePath : filePaths) {
            files.addAll(getListFiles(filePath));
        }
        return files.stream().sorted(new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减,如果 if 中修改为 返回1 同时此处修改为返回 -1  排序就会是递增,
            }
        }).collect(Collectors.toList());
    }

    /**
     * 复制文件并重命名
     *
     * @param filePaths
     * @return
     */
    public static synchronized String copyListFiles(List<String> filePaths) {
        List<File> files = getListFiles(filePaths);
        String uuid = UUID.fastUUID().toString();
        String filePath = Constants.TMP_HOME + uuid;
        FileUtil.mkdir(filePath);
        for (int i = 0; i < files.size(); i++) {
            FileUtil.copy(files.get(i), FileUtil.newFile(filePath + File.separator + i + ".jpg"), true);
        }
        return filePath + File.separator;
    }

    public static Date geLastWeekMonday(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, week - 8);
        return cal.getTime();
    }

    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    private static void start(List<String> commend) {
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

    @Override
    public void execute() {
        //判断是否结束归档
        if (endTime != null) {
            if (new Date().getTime() > endTime.getTime()) {
                CronUtil.remove(Constants.taskMap.get(taskId));
                Constants.taskMap.remove(taskId);
                return;
            }
        }


        log.info("开始任务归档" + taskId);
        String[] split = DateUtil.formatDate(DateUtil.yesterday()).split("-");
//        String[] split = DateUtil.formatDate(new Date()).split("-");
        List<String> sourcePaths = new ArrayList<>();
        String sourcePath = Constants.SCHEDULE_PATH + taskId + File.separator;
        String playPeriod = null;
        if (cycle == 0 && (type == 0 || type == 1)) {
            split = DateUtil.formatDate(new Date()).split("-");
            playPeriod = DateUtil.formatDate((new Date())) + " 至 " + DateUtil.formatDate((new Date()));
            String path = sourcePath + split[0] + File.separator + split[1] + File.separator + split[2];
            sourcePaths.add(path);
        }
        if (cycle == 0 && type == 2) {
            playPeriod = DateUtil.formatDate(DateUtil.yesterday()) + " 至 " + DateUtil.formatDate(DateUtil.yesterday());
            String path = sourcePath + split[0] + File.separator + split[1] + File.separator + split[2];
            sourcePaths.add(path);
        }
        if (cycle == 1) {
            playPeriod = DateUtil.formatDate(geLastWeekMonday(new Date(), 1)) + " " + DateUtil.formatDate(geLastWeekMonday(new Date(), 7));
            for (int i = 1; i <= 7; i++) {
                //取上周的年月日
                String[] dateTime = DateUtil.formatDate(geLastWeekMonday(new Date(), i)).split("-");
                String path = sourcePath + dateTime[0] + File.separator + dateTime[1] + File.separator + dateTime[2];
                sourcePaths.add(path);
            }
        }
        if (cycle == 2) {
            // 获取上个月的第一天和最后一天
            Calendar cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            String start = DateUtil.formatDate(cale.getTime());
            cale.set(Calendar.DAY_OF_MONTH, 0);
            playPeriod = start + " 至 " + DateUtil.formatDate(cale.getTime());
            String[] dateTime = DateUtil.formatDate(DateUtil.lastMonth()).split("-");
            String path = sourcePath + dateTime[0] + File.separator + dateTime[1] + File.separator + dateTime[2];
            sourcePaths.add(path);
        }
        if (cycle == 3) {
            Calendar cale = Calendar.getInstance();
            int currentYear = cale.get(Calendar.YEAR);
            cale.clear();
            cale.set(Calendar.YEAR, currentYear);
            cale.roll(Calendar.DAY_OF_YEAR, -1);
            Date currYearLast = cale.getTime();
            playPeriod = (DateUtil.year(new Date()) - 1) + "-01-01" + "至" + DateUtil.formatDate(currYearLast);
            String path = sourcePath + (Integer.parseInt(split[0]) - 1);
            sourcePaths.add(path);
        }
        String files = copyListFiles(sourcePaths);
        String outputName = DateUtil.format(new Date(), "YYYYMMddHHmmss");
        String outPutPath = Constants.MP4_PATH + taskId + File.separator + outputName + ".mp4";
        if (!FileUtil.exist(Constants.MP4_PATH + taskId)) {
            FileUtil.mkdir(Constants.MP4_PATH + taskId);
        }
        log.info("outPutPath = " + outPutPath);

        executeCmd(files, fps, outPutPath, musicPath);
        //生成点播地址
//        buildM3u8(outPutPath, Constants.HLS + outputName);

        //存放记录
        AlbumRecord albumRecord = new AlbumRecord();
        albumRecord.setTaskId(taskId);
        albumRecord.setPlayPeriod(playPeriod);
        albumRecord.setFilePath(outPutPath);
        albumRecord.setFileName(outputName + ".mp4");
        albumRecord.setCreatedUser(creatUser);
//        albumRecord.setFileAddress(Constants.HLS + outputName);
        //取文件夹第一张图片为封面
        String previewImage = Constants.PREVIEW_PATH + taskId + File.separator + outputName + "_pre.jpg";
        if (!FileUtil.exist(Constants.PREVIEW_PATH + taskId)) {
            FileUtil.mkdir(Constants.PREVIEW_PATH + taskId);
        }
        if (FileUtil.exist(files + "0.jpg")) {
            FileUtil.copy(files + "0.jpg", previewImage, true);
        }
        albumRecord.setPreviewImage(previewImage.replace(Constants.Root_PATH, ""));
        File file = new File(outPutPath);
        albumRecord.setFileSize(file.length() / 1024);//以k为单位
        double time = FileUtil.ls(sourcePath).length / Double.parseDouble(String.valueOf(fps));
        albumRecord.setDuration(time + "");
        albumRecordService.save(albumRecord);
//        FileUtil.del(files);
    }

}
