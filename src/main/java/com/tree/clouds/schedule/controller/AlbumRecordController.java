package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.tree.clouds.schedule.model.entity.ImageInfo;
import com.tree.clouds.schedule.model.vo.*;
import com.tree.clouds.schedule.service.AlbumRecordService;
import com.tree.clouds.schedule.service.ImageInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 相册记录 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/album-record")
@Api(value = "album-record", tags = "相册记录模块")
public class AlbumRecordController {
    @Autowired
    private AlbumRecordService albumRecordService;
    @Autowired
    private ImageInfoService imageInfoService;


    @PostMapping("/albumRecordPage")
    @ApiOperation(value = "相册记录分页查询")
    @Log("相册记录分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<AlbumRecord>> albumRecordPage(@RequestBody AlbumRecordPageVO albumRecordPageVO) {
        IPage<AlbumRecord> page = albumRecordService.albumRecordPage(albumRecordPageVO);
        return RestResponse.ok(page);
    }

    @GetMapping("/videoDownload")
    @ApiOperation(value = "相册记录下载")
    @Log("相册记录下载")
    @PreAuthorize("hasAuthority('login:log:list')")
    public void videoDownload(@RequestBody PublicIdReqVO publicIdReqVO, HttpServletResponse response) {
        albumRecordService.videoDownload(publicIdReqVO.getId(), response);
    }

    @PostMapping("/dateList")
    @ApiOperation(value = "相册图片列表")
    @Log("相册图片列表")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<ImageInfo>> dateList(@RequestBody ImageInfoVO imageInfoVO) {
        IPage<ImageInfo> page = imageInfoService.dateList(imageInfoVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/dateTree")
    @ApiOperation(value = "相册列表")
    @Log("相册列表")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<List<String>> dateTree(@RequestBody ImageInfoVO imageInfoVO) {
        List<String> dateTree = imageInfoService.dateTree(imageInfoVO);
        return RestResponse.ok(dateTree);
    }

    @PostMapping("/videoInfos")
    @ApiOperation(value = "视频列表")
    @Log("视频列表")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<List<AlbumRecord>> videoInfos(@RequestBody ImageInfoVO imageInfoVO) {
        List<AlbumRecord> albumRecords = albumRecordService.videoInfos(imageInfoVO.getScheduleId(), imageInfoVO.getDeviceId());
        return RestResponse.ok(albumRecords);
    }

    @PostMapping("/buildVideo")
    @ApiOperation(value = "自定义生成视频")
    @Log("自定义生成视频")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<String> buildVideo(@RequestBody BuildVideoVO buildVideoVO) {
        String taskId = albumRecordService.buildVideo(buildVideoVO);
        return RestResponse.ok(taskId);
    }

    @PostMapping("/removeVideo")
    @ApiOperation(value = "删除视频")
//    @Log("自定义生成视频")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> removeVideo(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        return RestResponse.ok(albumRecordService.removeVideo(publicIdsReqVO.getIds()));
    }


}

