package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.model.vo.MusicPageVO;

/**
 * <p>
 * 音乐表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-03-18
 */
public interface MusicMangerMapper extends BaseMapper<MusicManage> {

    IPage<MusicManage> musicPage(IPage<Object> page, MusicPageVO musicPageVO);

    int getMusicSum(String userId);
}
