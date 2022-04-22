package com.tree.clouds.schedule.common.ffmpeg;

import cn.hutool.core.io.FileUtil;
import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feiFan.gou on 2017/8/18 14:09.
 */
public class Info extends Base {

    @Test
    public void test() {
        Merge merge = new Merge();
        merge.audioJoin("D:\\application\\program\\ffmpeg-3.3.2-win64\\bin",
                Format.audio_type.WAV,
                "D:\\application\\program\\ffmpeg-3.3.2-win64\\bin\\test.wav",
                "D:\\application\\program\\ffmpeg-3.3.2-win64\\bin\\test1.wav",
                "D:\\application\\program\\ffmpeg-3.3.2-win64\\bin\\test2.wav");

    }


}
