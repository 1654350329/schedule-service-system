package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.model.vo.MusicPageVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 音乐表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-03-18
 */
public interface MusicManageService extends IService<MusicManage> {

    IPage<MusicManage> musicPage(MusicPageVO musicPageVO);

    void uploadMusic(MultipartFile file);

    int getMusicSum(Integer type);
}
