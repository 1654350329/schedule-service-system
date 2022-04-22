package com.tree.clouds.schedule.common.ffmpeg;

import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import com.tree.clouds.schedule.common.ffmpeg.kit.CommonKit;
import org.junit.Test;

import java.io.File;

/**
 * Created by feiFan.gou on 2017/8/18 16:26.
 */
public class Image extends Base {

    /**
     * 视频转化成Gif
     *
     * @param sourcePath 原视频
     * @param outputPath 输出路径
     * @param frame      截取的帧
     */
    public void toGif(String sourcePath, String outputPath, int frame) {

        if (!CommonKit.checkParam(sourcePath, outputPath)) {
            throw new RuntimeException("参数不能为空");
        }
        if (!CommonKit.fileExist(sourcePath)) {
            throw new RuntimeException("视频文件不存在");
        }
        if (!CommonKit.fileExist(outputPath)) {
            throw new RuntimeException("输出路径不存在");
        }
        if (frame <= 0) {
            throw new RuntimeException("截取帧数必须大于0");
        }
        outputPath += (File.separator + "from_video_gif.gif");
        String command = String.format("-i %s -vframes %d -f gif %s -y -v quiet", sourcePath, frame, outputPath);
//        executeFFMPeg(command);

    }

    /**
     * @param sourcePath 原视频
     * @param outputPath 输出路径
     * @param time       截取哪秒的图
     * @param size       截图片的尺寸
     */
    public void toImage(String sourcePath, String outputPath, double time, String size) {

        if (!CommonKit.checkParam(sourcePath, outputPath)) {
            throw new RuntimeException("参数不能为空");
        }
        if (!CommonKit.fileExist(sourcePath)) {
            throw new RuntimeException("视频文件不存在");
        }
        if (!CommonKit.fileExist(outputPath)) {
            throw new RuntimeException("输出路径不存在");
        }
        if (time <= 0) {
            throw new RuntimeException("截取图片时间必须大于0");
        }
        outputPath += (File.separator + "from_video_image.jpg");
        if (CommonKit.isNotEmpty(size)) {
            size = "-s " + size;
        } else {
            size = CommonKit.empty;
        }
        String command = String.format("-i %s -f image2 -ss %f -t 0.001 %s %s -y -v quiet", sourcePath, time, size, outputPath);
//        executeFFMPeg(command);
    }

    @Test
    public void test() {
        String sourcePath = "E:\\001.mp4";
        toImage(sourcePath,
                "E:\\", 10.004, "320x180");
    }
}
