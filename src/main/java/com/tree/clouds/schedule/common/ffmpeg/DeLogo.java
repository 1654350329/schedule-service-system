package com.tree.clouds.schedule.common.ffmpeg;

import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import org.junit.Test;

/**
 * Created by feiFan.gou on 2018/1/3 16:42.
 */
class DeLogo extends Base {


    @Test
    void test() {

        String videoPath = "C:\\Users\\feifan.gou\\Desktop\\download\\A.mp4";

        String cmd = "-i " + videoPath + " -vf delogo=x=20:y=10:w=168:h=86:show=0 output.mp4";
//        executeFFMPeg(cmd);

    }
}
