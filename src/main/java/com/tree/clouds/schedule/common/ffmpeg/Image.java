package com.tree.clouds.schedule.common.ffmpeg;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import com.tree.clouds.schedule.common.ffmpeg.kit.CommonKit;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

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
//        ImgUtil.pressText(//
//                FileUtil.file("D:\\123.jpg"), //
//                FileUtil.file("D:\\123.jpg"), //
//                "版权所有", Color.WHITE, //文字
//                new Font("黑体", Font.BOLD, 100), //字体
//                -100, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
//                -100, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
//                0.5f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
//        );
//        ImgUtil.pressImage(
//                FileUtil.file("D:\\123.jpg"),
//                FileUtil.file("D:\\12346546.jpg"),
//                ImgUtil.read(FileUtil.file("D:\\1212.jpeg")), //水印图片
//                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
//                -500, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
//                0.8f
//        );

        try {
            //左上角
            File file = new File("D:\\20220323175524.jpg");
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
            int x = (int) (sourceImg.getWidth() / 1.7 - sourceImg.getWidth());
            int y = (int) (sourceImg.getHeight() / 1.6 - sourceImg.getHeight());
            System.out.println("y = " + y);
            System.out.println("x = " + x);
            //左下角
            y = (int) (sourceImg.getHeight() - sourceImg.getHeight() / 1.6);
            //右上角
            x = (int) (sourceImg.getWidth() - sourceImg.getWidth() / 1.7);
            //右下角
            System.out.println(sourceImg.getWidth()); // 源图宽度
            System.out.println(sourceImg.getHeight()); // 源图高度
            ImgUtil.pressText(//
                    FileUtil.file("D:\\20220323175524.jpg"), //
                    FileUtil.file("D:\\" + new Date().getTime() + ".jpg"), //
                    "版权所有11", Color.BLACK, //文字
                    new Font("黑体", Font.BOLD, sourceImg.getHeight() / 15), //字体
                    x, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                    y, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                    1f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
