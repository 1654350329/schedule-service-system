package com.tree.clouds.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.ImageInfoMapper;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ImageInfo;
import com.tree.clouds.schedule.model.vo.ImageInfoVO;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.service.ImageInfoService;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图片列表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-04-12
 */
@Service
public class ImageInfoServiceImpl extends ServiceImpl<ImageInfoMapper, ImageInfo> implements ImageInfoService {

    @Autowired
    private DeviceScheduleService deviceScheduleService;


    @Override
    public IPage<ImageInfo> dateList(ImageInfoVO imageInfoVO) {
        IPage<ImageInfo> page = imageInfoVO.getPage();
        DeviceSchedule deviceSchedule = deviceScheduleService.getByScheduleIdAndDeviceId(imageInfoVO.getScheduleId(), imageInfoVO.getDeviceId());
        page = this.baseMapper.getByDate(page, deviceSchedule.getTaskId(), imageInfoVO.getYear(), imageInfoVO.getMonth(), imageInfoVO.getDay());
        return page;
    }

    @Override
    public List<ImageInfo> imageInfos(List<String> ids) {
        return this.listByIds(ids);
    }

    @Override
    public List<String> dateTree(ImageInfoVO imageInfoVO) {
        DeviceSchedule deviceSchedule = deviceScheduleService.getByScheduleIdAndDeviceId(imageInfoVO.getScheduleId(), imageInfoVO.getDeviceId());
        if (deviceSchedule == null) {
            return null;
        }
        List<ImageInfoVO> date = this.baseMapper.getDate(deviceSchedule.getTaskId());
        List<String> dateTree = new ArrayList<>();
        for (ImageInfoVO infoVO : date) {
            String key = infoVO.getYear() + "-" + infoVO.getMonth() + "-" + infoVO.getDay();
            dateTree.add(key);
        }
        return dateTree;
    }

    @Override
    public int getImageSum(String taskId) {
        QueryWrapper<ImageInfo> wrapper = new QueryWrapper<ImageInfo>().eq(ImageInfo.CREATED_USER, LoginUserUtil.getUserId());
        if (taskId != null) {
            wrapper.eq(ImageInfo.TASK_ID, taskId);
        }
        return this.count(wrapper);
    }
}
