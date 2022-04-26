package com.tree.clouds.schedule.common.ffmpeg;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import org.junit.Test;

import java.awt.*;

/**
 * Created by feiFan.gou on 2018/1/3 16:42.
 */
class DeLogo extends Base {


    public static void main(String[] args) {
        ImgUtil.pressText(//
                FileUtil.file("D:\\132.jpg"), //
                FileUtil.file("D:\\132_result.jpg"), //
                "版权所有", Color.WHITE, //文字
                new Font("黑体", Font.BOLD, 100), //字体
                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
        );
    }

    @Test
    void test() {

        String videoPath = "C:\\Users\\feifan.gou\\Desktop\\download\\A.mp4";

        String cmd = "-i " + videoPath + " -vf delogo=x=20:y=10:w=168:h=86:show=0 output.mp4";
//        executeFFMPeg(cmd);

    }
}
