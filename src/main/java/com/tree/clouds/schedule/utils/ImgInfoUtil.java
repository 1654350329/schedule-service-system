package com.tree.clouds.schedule.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AUTHOR cjn
 * TIME  2018-4-16 11:08
 * REMARK 图片处理类 压缩图片大小
 */
public class ImgInfoUtil {


    static Logger logger = LoggerFactory.getLogger(ImgInfoUtil.class);

    /**
     * 左上角
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, Integer> getTopLeftInfo(File file) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        //左上角
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        int x = (int) (sourceImg.getWidth() / 1.7 - sourceImg.getWidth());
        int y = (int) (sourceImg.getHeight() / 1.6 - sourceImg.getHeight());
        map.put("x", x);
        map.put("y", y);
        return map;
    }

    /**
     * 左下角
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, Integer> getBottomLeftInfo(File file) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        //左下角
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        int x = (int) (sourceImg.getWidth() / 1.7 - sourceImg.getWidth());
        int y = (int) (sourceImg.getHeight() - sourceImg.getHeight() / 1.6);
        map.put("x", x);
        map.put("y", y);
        return map;
    }

    /**
     * 右上角
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, Integer> getTopRightInfo(File file) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        //左上角
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        int x = (int) (sourceImg.getWidth() - sourceImg.getWidth() / 1.7);
        int y = (int) (sourceImg.getHeight() / 1.6 - sourceImg.getHeight());
        map.put("x", x);
        map.put("y", y);
        return map;
    }

    /**
     * 右下角
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map<String, Integer> getBottomRightInfo(File file) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
        int y = (int) (sourceImg.getHeight() - sourceImg.getHeight() / 1.6);
        int x = (int) (sourceImg.getWidth() - sourceImg.getWidth() / 1.7);
        map.put("x", x);
        map.put("y", y);
        return map;
    }

    public static Map<String, Integer> getInfo(File file, Integer coordinateType) {
        try {
            if (coordinateType == 1) {
                return getTopLeftInfo(file);
            }
            if (coordinateType == 2) {
                return getBottomLeftInfo(file);
            }
            if (coordinateType == 3) {
                return getTopRightInfo(file);
            }
            return getBottomRightInfo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("x", 0);
        map.put("y", 0);
        return map;
    }
}