package com.tree.clouds.schedule.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class DownloadFile {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final List<String> WATERMARK_FILES = Arrays.asList("doc", "docx", "xlsx", "pdf");
    private static final String SEARCH_STYLE =
            "<style type=\"text/css\">" +
                    ".fhighlight{background-color:#FF6F68;}.shighlight{background-color:#FFC937;}" +
                    ".on{color:red;text-decoration:underline;}.blue{background-color:#7CB5F2;}" +
                    "</style>";
    private static final String SEARCH_SCRIPT =
            "<script type=\"text/javascript\">" +
                    "function searchVal(e){var t=e;$(\"body\").unhighlight({element:\"span\",className:\"shighlight\"})," +
                    "$(\"body\").highlight(t,{element:\"span\",className:\"shighlight\"})}" +
                    "function getQueryVariable(e){for(var t=window.location.search.substring(1).split(\"&\")," +
                    "i=0;i<t.length;i++){var r=t[i].split(\"=\");if(r[0]==e)return r[1]}return!1}" +
                    "function current(e,t){var i=\"\";if(i=1==t?$(\".shighlight\"):$($(\".shighlight\").toArray().reverse()),e){" +
                    "var r=$(\".blue\");if(r[0]){if(r.text()!=e)return r.removeClass(\"blue\"),current(e,t),!1;var l=!1;" +
                    "i.each(function(t){var i=$(this);if(i.text()==e&&l)return i.addClass(\"blue\")," +
                    "window.scrollTo(0,i.offset().top-400),!1;i.text()==e&&i.hasClass(\"blue\")&&(i.removeClass(\"blue\")," +
                    "l=!0)})}else i.each(function(){var t=$(this);if(t.text()==e)return t.addClass(\"blue\")," +
                    "window.scrollTo(0,t.offset().top-400),!1})}}$(function(){var e=getQueryVariable(\"filterVal\");" +
                    "$(\"body\").highlight(decodeURIComponent(e),{element:\"span\",className:\"fhighlight\"})});" +
                    "</script>";

    /**
     * 下载文件
     *
     * @param bytes    byte[]数组
     * @param fileName 文件名（带后缀）
     * @param response
     * @throws IOException
     */
    public static void downloadFile(byte[] bytes, String fileName, HttpServletResponse response) {
        if (ObjectUtil.isEmpty(bytes) || bytes.length == 0) {
            throw new com.tree.clouds.schedule.utils.BaseBusinessException(500, "文件不存在");
        }
        if (ObjectUtil.isEmpty(fileName)) {
            throw new com.tree.clouds.schedule.utils.BaseBusinessException(500, "下载文件名为空");
        }
        try (InputStream in = new ByteArrayInputStream(bytes);
             OutputStream out = response.getOutputStream()) {
            //获取文件名
            String filename = getEncoder(fileName, null);
            //设置响应头，控制浏览器下载该文件
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            response.setHeader("FileName", filename);
            String fileType = FileUtil.extName(fileName);
            response.setContentLength(bytes.length);
            //缓存
            IoUtil.copy(in, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param inputStream 文件流
     * @param fileName    文件名（带后缀）
     * @param response
     * @throws IOException
     */
    public static void downloadFile(InputStream inputStream, String fileName, HttpServletResponse response) {
        downloadFile(inputStream, fileName, response, true);
    }

    /**
     * 下载文件
     *
     * @param inputStream 文件流
     * @param fileName    文件名（带后缀）
     * @param response
     * @param textWater   是否添加水印
     * @throws IOException
     */
    public static void downloadFile(InputStream inputStream, String fileName, HttpServletResponse response, boolean textWater) {

        if (ObjectUtil.isNull(inputStream)) {
            throw new com.tree.clouds.schedule.utils.BaseBusinessException(500, "文件不存在");
        }
        if (ObjectUtil.isEmpty(fileName)) {
            throw new com.tree.clouds.schedule.utils.BaseBusinessException(500, "下载文件名为空");
        }
        try (OutputStream out = response.getOutputStream()) {
            //获取文件名
            String filename = getEncoder(fileName, null);
//            response.setContentType("application/x-msdownload"); //设置响应类型,此处为下载类型
//            response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+"\"");//以附件的形式打开
            //设置响应头，控制浏览器下载该文件
            //判断浏览器代理并分别设置响应给浏览器的编码格式
//            response.setContentType("application/vnd.ms-excel");
//            response.addHeader("Content-Disposition" ,"attachment;filename=\"" +finalFileName+ "\"");
//            response.addHeader("Cache-Control"," max-age=0");
//            response.addHeader("Cache-Control", "max-age=1");
            //设置HTTP响应头
//            response.reset();//重置 响应头
//            response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
//            response.addHeader("Content-Disposition" ,"attachment;filename=\"" +finalFileName+ "\"");//下载文件的名称

            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            response.setHeader("FileName", filename);
            //缓存区
            IoUtil.copy(inputStream, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param bytes       byte[]数组
     * @param fileName    文件名（带后缀）
     * @param response
     * @param isWatermark 是否加水印
     * @throws IOException
     */
    public static void downloadFile(byte[] bytes, String fileName, HttpServletResponse response, boolean isWatermark) {
        if (ObjectUtil.isEmpty(bytes) || bytes.length == 0) {
            getMsg(response, "该文件不存在或已被删除");
        }
        if (ObjectUtil.isEmpty(fileName)) {
            getMsg(response, "下载文件名为空");
        }
        //获取文件名
        String filename = getEncoder(fileName, null);
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            //设置响应头，控制浏览器下载该文件
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setHeader("FileName", filename);
            response.setContentLength(bytes.length);

            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 预览文件
     * 传文件转html
     *
     * @param bytes      文件byte
     * @param fileSuffix 文件后缀
     */
    public static void preview(byte[] bytes, String fileSuffix, HttpServletResponse response) throws IOException {
        if (ObjectUtil.isEmpty(bytes)) {
            getMsg(response, "文件不存在或已被删除");
            throw new com.tree.clouds.schedule.utils.BaseBusinessException(500, "文件不存在");
        }
        String fileSuffixLower = fileSuffix.toLowerCase();
        InputStream inputStream = IoUtil.toStream(bytes);
        IoUtil.copy(inputStream, response.getOutputStream());
    }

    /**
     * 写入并返回请求头
     *
     * @param response
     * @param name
     */
    public static void getMsg(HttpServletResponse response, String name) {
        String encoderString = getEncoder(name, null);
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;msg=" + encoderString);
        response.setHeader("Access-Control-Expose-Headers", "msg");
        response.setHeader("msg", encoderString);
    }

    public static String getEncoder(String fileName, String encoding) {
        if (StrUtil.isEmpty(encoding)) {
            encoding = "utf-8";
        }
        StringBuilder encoderString = new StringBuilder();
        try {
            for (int i = 0; i < fileName.length(); i++) {
                if ((fileName.charAt(i) + "").getBytes().length > 1) {
                    encoderString.append(URLEncoder.encode(fileName.charAt(i) + "", encoding));
                } else {
                    encoderString.append(fileName.charAt(i));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoderString.toString();
    }

    /**
     * 将文件转换成byte数组
     *
     * @param tradeFile
     * @return
     */
    public static byte[] File2byte(File tradeFile) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(tradeFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
