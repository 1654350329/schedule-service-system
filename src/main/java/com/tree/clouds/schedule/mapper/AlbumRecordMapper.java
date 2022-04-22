package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.vo.AlbumRecordPageVO;

/**
 * <p>
 * 相册记录 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface AlbumRecordMapper extends BaseMapper<AlbumRecord> {

    IPage<AlbumRecord> albumRecordPage(IPage<AlbumRecord> page, AlbumRecordPageVO albumRecordPageVO, String taskId);
}
