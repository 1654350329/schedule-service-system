package com.tree.clouds.schedule.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.model.vo.MusicPageVO;
import com.tree.clouds.schedule.model.vo.PublicIdsReqVO;
import com.tree.clouds.schedule.service.MusicManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/music-manage")
@Api(value = "music-manage", tags = "音乐模块")
public class MusicController {

    @Autowired
    private MusicManageService musicMangerService;

    @PostMapping("/musicPage")
    @ApiOperation(value = "音乐模块分页查询")
    @Log("音乐模块分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<MusicManage>> musicPage(@RequestBody MusicPageVO musicPageVO) {
        IPage<MusicManage> page = musicMangerService.musicPage(musicPageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/uploadMusic")
    @ApiOperation(value = "上传音乐")
    @Log("音乐模块分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> uploadMusic(@RequestParam("file") List<MultipartFile> files) {
        for (MultipartFile file : files) {
            musicMangerService.uploadMusic(file);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/removeMusic")
    @ApiOperation(value = "删除音乐")
    @Log("音乐模块分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<Boolean> removeMusic(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        List<MusicManage> collect = publicIdsReqVO.getIds().stream().map(id -> {
            MusicManage musicManage = new MusicManage();
            musicManage.setMusicId(id);
            musicManage.setDel(1);
            return musicManage;
        }).collect(Collectors.toList());
        musicMangerService.updateBatchById(collect);
        return RestResponse.ok(true);
    }

}
