package com.tree.clouds.schedule.utils;

import java.io.File;

public class SystemUtil {
    /**
     * 直接获取系统名称 区分
     */

    public static String getSystemByName() {
        String osName = System.getProperties().getProperty("os.name");
        System.out.println("osName = " + osName);
        if (osName.equals("Linux")) {
            return "linux";
        } else {
            return "other";
        }

    }

    /**
     * 根据反斜杠区分
     */

    public static String getSystemByPath() {
        if (File.separator.equals("/")) {
            return "linux";
        } else {
            return "other";
        }
    }
}
