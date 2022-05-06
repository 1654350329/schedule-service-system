package com.tree.clouds.schedule.controller;

import cn.hutool.core.util.StrUtil;
import com.tree.clouds.schedule.common.NonStaticResourceHttpRequestHandler;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.service.AlbumRecordService;
import com.tree.clouds.schedule.service.MusicManageService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
@Api(value = "file", tags = "视频播放模块")
@AllArgsConstructor
public class FileRestController {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Autowired
    private AlbumRecordService albumRecordService;
    @Autowired
    private MusicManageService musicManageService;


    @GetMapping("/video/{id}")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        AlbumRecord albumRecord = albumRecordService.getById(id);
        String realPath = albumRecord.getFilePath();
        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StrUtil.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/music/{id}")
    public void musicPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
        MusicManage musicManage = musicManageService.getById(id);
        String realPath = musicManage.getFilePath();
        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StrUtil.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

}