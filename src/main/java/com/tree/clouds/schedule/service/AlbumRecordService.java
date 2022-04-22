package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.tree.clouds.schedule.model.vo.AlbumRecordPageVO;
import com.tree.clouds.schedule.model.vo.BuildVideoVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 相册记录 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface AlbumRecordService extends IService<AlbumRecord> {

    IPage<AlbumRecord> albumRecordPage(AlbumRecordPageVO albumRecordPageVO);

    void videoDownload(String id, HttpServletResponse response);

    List<AlbumRecord> videoInfos(String scheduleId, String deviceId);

    String buildVideo(BuildVideoVO buildVideoVO);

    /**
     * 根据id是否已经生成视频
     *
     * @param recordId
     */
    Boolean buildVideo(String recordId);

    int getRecordSum(String taskId);

}
