package com.tree.clouds.schedule.common.ffmpeg;

import com.tree.clouds.schedule.common.ffmpeg.base.Base;
import com.tree.clouds.schedule.common.ffmpeg.kit.CommonKit;
import org.junit.Test;

import java.io.File;

/**
 * Created by feiFan.gou on 2017/8/18 14:00.
 */
public class Format extends Base {

    @Test
    public void testFormat() {

//        audio2video("D:\\downloads\\music\\test.mp3","D:\\downloads", "640x360", video_type.AVI);
        video2audio("C:\\Users\\feifan.gou\\Desktop\\download\\JTSJ0090.mp4",
                "C:\\Users\\feifan.gou\\Desktop\\download", audio_type.WAV);
    }

    /**
     * 音频转视频
     *
     * @param sourcePath 原音频文件
     * @param outputPath 输出路径
     * @param size       视频尺寸
     * @param videoType  输出视频的路径
     */
    public void audio2video(String sourcePath, String outputPath, String size, video_type videoType) {

        if (!CommonKit.checkParam(sourcePath, outputPath, size)) {
            throw new RuntimeException("参数不能为空");
        }
        if (!CommonKit.fileExist(sourcePath)) {
            throw new RuntimeException("原视频不存在");
        }
        if (!CommonKit.fileExist(outputPath)) {
            throw new RuntimeException("输出路径不存在");
        }
        outputPath += (File.separator + "audio_to_video" + videoType.suffix);
        String command = String.format("-i %s -ab 56 -ar 22050 -b 500 -r 15 -s %s %s", sourcePath, size, outputPath);
//        executeFFMPeg(command);
    }

    /**
     * @param sourcePath 原音频文件
     * @param outputPath 输出路径
     * @param audioType  输出音频的格式
     */
    public void video2audio(String sourcePath, String outputPath, audio_type audioType) {

        if (!CommonKit.checkParam(sourcePath, outputPath)) {
            throw new RuntimeException("参数不能为空");
        }
        if (!CommonKit.fileExist(sourcePath)) {
            throw new RuntimeException("原视频不存在");
        }
        if (!CommonKit.fileExist(outputPath)) {
            throw new RuntimeException("输出路径不存在");
        }
        outputPath += (File.separator + "video_to_audio" + audioType.suffix);
        String command = String.format(" -i %s -f %s -vn %s", sourcePath, audioType.name().toLowerCase(), outputPath);
//        executeFFMPeg(command);
    }


    public enum video_type {

        MP4(".mp4"),
        MKV(".mkv"),
        MOV(".mov"),
        AVI(".avi"),
        WMV(".wmv"),
        FLV(".flv"),
        _3GP(".3GP"),
        F4V(".f4v"),
        RMVB(".rmvb");

        public final String suffix;

        video_type(String suffix) {
            this.suffix = suffix;
        }
    }

    public enum audio_type {

        MP3(".mp3"),
        WAV(".wav"),
        WMA(".wma");

        public final String suffix;

        audio_type(String suffix) {
            this.suffix = suffix;
        }
    }

//    public void


}
