package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.common.ffmpeg.Move;
import com.tree.clouds.schedule.mapper.AlbumRecordMapper;
import com.tree.clouds.schedule.model.entity.AlbumRecord;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.MusicManage;
import com.tree.clouds.schedule.model.vo.AlbumRecordPageVO;
import com.tree.clouds.schedule.model.vo.BuildVideoVO;
import com.tree.clouds.schedule.service.AlbumRecordService;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.service.MusicManageService;
import com.tree.clouds.schedule.utils.DownloadFile;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 相册记录 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Service
public class AlbumRecordServiceImpl extends ServiceImpl<AlbumRecordMapper, AlbumRecord> implements AlbumRecordService {

    @Autowired
    private DeviceScheduleService deviceScheduleService;
    @Autowired
    private MusicManageService musicManageService;


    @Override
    public IPage<AlbumRecord> albumRecordPage(AlbumRecordPageVO albumRecordPageVO) {
        DeviceSchedule deviceSchedule = this.deviceScheduleService.getByScheduleIdAndDeviceId(albumRecordPageVO.getScheduleId(), albumRecordPageVO.getDeviceId());
        IPage<AlbumRecord> page = albumRecordPageVO.getPage();
        return this.baseMapper.albumRecordPage(page, albumRecordPageVO, deviceSchedule.getTaskId());
    }

    @Override
    public void videoDownload(String id, HttpServletResponse response) {
        AlbumRecord albumRecord = this.getById(id);
        byte[] bytes = DownloadFile.File2byte(new File(albumRecord.getFilePath()));
        DownloadFile.downloadFile(bytes, albumRecord.getFileName(), response, false);

    }


    public List<AlbumRecord> videoInfos(String scheduleId, String deviceId) {
        DeviceSchedule deviceSchedule = this.deviceScheduleService.getByScheduleIdAndDeviceId(scheduleId, deviceId);
        if (deviceSchedule == null) {
            return null;
        }
        return this.list(new QueryWrapper<AlbumRecord>().eq(AlbumRecord.TASK_ID, deviceSchedule.getTaskId()));
    }

    @Override
    public String buildVideo(BuildVideoVO buildVideoVO) {
        MusicManage musicManage = musicManageService.getById(buildVideoVO.getMusicId());
        DeviceSchedule service = deviceScheduleService.getByScheduleIdAndDeviceId(buildVideoVO.getScheduleId(), buildVideoVO.getDeviceId());
        String sourcePath = Constants.SCHEDULE_PATH + service.getTaskId() + File.separator;
        List<String> sourcePaths = new ArrayList<>();
        for (String date : buildVideoVO.getData()) {
            String[] split = date.split("-");
            String path = sourcePath + split[0] + File.separator + split[1] + File.separator + split[2];
            sourcePaths.add(path);
        }
        String files = Move.copyListFiles(sourcePaths);
        String outputName = DateUtil.format(new Date(), "YYYYMMddHHmmss");
        String outPutPath = Constants.MP4_PATH + service.getTaskId() + File.separator + outputName + ".mp4";
        if (!FileUtil.exist(Constants.MP4_PATH + service.getTaskId())) {
            FileUtil.mkdir(Constants.MP4_PATH + service.getTaskId());
        }
        //存放记录
        AlbumRecord albumRecord = new AlbumRecord();
        albumRecord.setTaskId(service.getTaskId());
        albumRecord.setPlayPeriod(null);
        albumRecord.setFilePath(outPutPath);
        albumRecord.setFileName(outputName + ".mp4");
        albumRecord.setDel(1);
//        albumRecord.setFileAddress(Constants.HLS + outputName);
        //取文件夹第一张图片为封面
        String previewImage = Constants.PREVIEW_PATH + service.getTaskId() + File.separator + outputName + "_pre.jpg";
        if (!FileUtil.exist(Constants.PREVIEW_PATH + service.getTaskId())) {
            FileUtil.mkdir(Constants.PREVIEW_PATH + service.getTaskId());
        }
        if (FileUtil.exist(files + "0.jpg")) {
            FileUtil.copy(files + "0.jpg", previewImage, true);
        }
        albumRecord.setPreviewImage(previewImage.replace(Constants.Root_PATH, ""));
        File file = new File(outPutPath);
        albumRecord.setFileSize(file.length() / 1024);//以k为单位
        double time = FileUtil.ls(sourcePath).length / Double.parseDouble(buildVideoVO.getFps() + "");
        albumRecord.setDuration(time + "");
        this.save(albumRecord);
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                Move.executeCmd(files, buildVideoVO.getFps(), outPutPath, musicManage.getFilePath());
                AlbumRecord al = new AlbumRecord();
                al.setRecordId(albumRecord.getTaskId());
                al.setDel(0);
                updateById(al);
            }
        });
        return albumRecord.getRecordId();
    }

    @Override
    public Boolean buildVideo(String recordId) {
        AlbumRecord albumRecord = this.getById(recordId);
        if (FileUtil.exist(albumRecord.getFilePath())) {
            return true;
        }
        return false;
    }

    @Override
    public int getRecordSum(String taskId) {
        QueryWrapper<AlbumRecord> wrapper = new QueryWrapper<AlbumRecord>().eq(AlbumRecord.CREATED_USER, LoginUserUtil.getUserId());
        if (taskId != null) {
            wrapper.eq(AlbumRecord.TASK_ID, taskId);
        }
        return this.count(wrapper);
    }

    @Override
    public Boolean removeVideo(List<String> ids) {
        for (String id : ids) {
            AlbumRecord albumRecord = this.getById(id);
            FileUtil.del(albumRecord.getFilePath());
            this.removeById(id);
        }
        return true;
    }

}
