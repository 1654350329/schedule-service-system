package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.MusicMangerMapper;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.model.vo.MusicPageVO;
import com.tree.clouds.schedule.service.MusicManageService;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 音乐表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-18
 */
@Service
public class MusicManageServiceImpl extends ServiceImpl<MusicMangerMapper, MusicManage> implements MusicManageService {

    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        try {
            // 获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IPage<MusicManage> musicPage(MusicPageVO musicPageVO) {
        return this.baseMapper.musicPage(musicPageVO.getPage(), musicPageVO);
    }

    @Override
    public void uploadMusic(MultipartFile multipartFile) {
        File file = MultipartFileToFile(multipartFile);
        if (file != null) {
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            FileUtil.mkdir(Constants.MUSIC_PATH);
            File newFile = new File(Constants.MUSIC_PATH + UUID.fastUUID() + "." + prefix);
            FileUtil.move(file, newFile, true);
            MusicManage musicManage = new MusicManage();
            musicManage.setFileName(multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf(".")));
            musicManage.setFilePath(newFile.getAbsolutePath());
            Double size = (double) newFile.length() / 1024 / 1024;
            musicManage.setFileSize(String.format("%.1f", size) + "M");
            musicManage.setFileType(prefix);
            this.save(musicManage);
        }

    }

    @Override
    public int getMusicSum(Integer type) {
        if (type != null) {
            return this.count(new QueryWrapper<MusicManage>().eq(MusicManage.CREATED_USER, LoginUserUtil.getUserId()));
        } else {
            return this.baseMapper.getMusicSum(LoginUserUtil.getUserId());
        }
    }
}
