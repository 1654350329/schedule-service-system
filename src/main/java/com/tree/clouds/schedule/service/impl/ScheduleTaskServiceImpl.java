package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.cron.CronUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.ScheduleTaskMapper;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;
import com.tree.clouds.schedule.model.vo.DeviceScheduleVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskPageVO;
import com.tree.clouds.schedule.model.vo.ScheduleTaskVO;
import com.tree.clouds.schedule.service.*;
import com.tree.clouds.schedule.task.TaskSchedule;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import com.tree.clouds.schedule.utils.MultipartFileUtil;
import com.tree.clouds.schedule.utils.SunRiseSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * 计划配置 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Service
@Slf4j
public class ScheduleTaskServiceImpl extends ServiceImpl<ScheduleTaskMapper, ScheduleTask> implements ScheduleTaskService {

    @Autowired
    private DeviceScheduleService deviceScheduleService;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private AlbumRecordService albumRecordService;

    @Override
    public IPage<ScheduleTaskBO> schedulePage(ScheduleTaskPageVO scheduleTaskPageVO) {
        IPage<ScheduleTaskBO> page = scheduleTaskPageVO.getPage();
        page = this.baseMapper.schedulePage(page, scheduleTaskPageVO);
        for (ScheduleTaskBO record : page.getRecords()) {
            if (scheduleTaskPageVO.getDeviceId() != null) {
                DeviceSchedule deviceSchedule = this.deviceScheduleService.getByScheduleIdAndDeviceId(record.getScheduleId(), scheduleTaskPageVO.getDeviceId());
                record.setImageNumber(this.imageInfoService.getImageSum(deviceSchedule.getTaskId()));
                record.setVideoNumber(this.albumRecordService.getRecordSum(deviceSchedule.getTaskId()));
            }
            //跨夜计划 展示为自定义计划
            if (record.getTaskType() == 7) {
                record.setTaskType(2);
            }
            if (record.getTaskType() == 2) {
                record.setTime(record.getStartTime() + " 至 " + record.getEndTime());
            }
            //设置运行时间
            String sunrise = SunRiseSet.getSunrise(new BigDecimal("119.223983"), new BigDecimal("27.413"), new Date());
            String sunset = SunRiseSet.getSunset(new BigDecimal("119.223983"), new BigDecimal("27.413"), new Date());
            //日出计划
            if (record.getTaskType() == 0) {
                record.setTime(sunrise + " 至 " + DateUtil.formatTime(new Date(DateUtil.parseTime(sunrise).getTime() + 1000 * 60 * 60)));
            }
            //日落计划
            if (record.getTaskType() == 1) {
                record.setTime(sunset + " 至 " + DateUtil.formatTime(new Date(DateUtil.parseTime(sunset).getTime() + 1000 * 60 * 60)));
            }
            //满月计划黑夜计划
            if (record.getTaskType() == 3 || record.getTaskType() == 6) {
                sunrise = SunRiseSet.getSunrise(new BigDecimal("119"), new BigDecimal("27.413"), DateUtil.tomorrow());
                record.setTime(sunset + " 至 " + sunrise);
            }
            //白天计划
            if (record.getTaskType() == 4) {
                record.setTime(sunrise + " 至 " + sunset);
            }
            record.setDate(record.getStartDate() + " 至 " + record.getEndDate());
            int size = deviceScheduleService.getByScheduleId(record.getScheduleId()).size();
            record.setDeviceNumber(size);
        }
        return page;
    }

    @Override
    public void updateSchedule(ScheduleTaskVO scheduleTaskVO) {
        ScheduleTask task = this.getById(scheduleTaskVO.getScheduleId());
        ScheduleTask scheduleTask = BeanUtil.toBean(scheduleTaskVO, ScheduleTask.class);
        if (task.getScheduleStatus() != null && task.getScheduleStatus() == 1) {
            throw new BaseBusinessException(400, "任务已开启,请先停止任务!");
        }
        ScheduleTask one = this.getOne(new QueryWrapper<ScheduleTask>().eq(ScheduleTask.SCHEDULE_NAME, scheduleTaskVO.getScheduleName())
                .ne(ScheduleTask.SCHEDULE_ID, scheduleTaskVO.getScheduleId()));
        if (one != null) {
            throw new BaseBusinessException(400, "计划任务名称已存在");
        }
        this.updateById(scheduleTask);
    }

    @Override
    public void addSchedule(ScheduleTaskVO scheduleTaskVO) {
        ScheduleTask one = this.getOne(new QueryWrapper<ScheduleTask>().eq(ScheduleTask.SCHEDULE_NAME, scheduleTaskVO.getScheduleName()));
        if (one != null) {
            throw new BaseBusinessException(400, "计划任务名称已存在");
        }
        ScheduleTask scheduleTask = BeanUtil.toBean(scheduleTaskVO, ScheduleTask.class);
        scheduleTask.setScheduleStatus(0);
        this.save(scheduleTask);
    }

    @Override
    public void deleteSchedule(List<String> ids) {
        List<ScheduleTask> collect = this.listByIds(ids).stream().filter(scheduleTask -> scheduleTask.getScheduleStatus() != null && scheduleTask.getScheduleStatus() == 1).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(collect)) {
            throw new BaseBusinessException(400, "任务已开启,请先终止!");
        }
        List<String> taskIds = new ArrayList<>();
        for (String id : ids) {
            List<DeviceSchedule> deviceSchedules = this.deviceScheduleService.getByScheduleId(id);
            for (DeviceSchedule deviceSchedule : deviceSchedules) {
                //删除图片
                FileUtil.del(Constants.SCHEDULE_PATH + deviceSchedule.getTaskId());
                //删除视频
                FileUtil.del(Constants.MP4_PATH + deviceSchedule.getTaskId());
                taskIds.add(deviceSchedule.getTaskId());
            }
        }
        deviceScheduleService.removeByIds(taskIds);
        this.removeByIds(ids);
    }

    @Override
    public void configureSchedule(List<DeviceScheduleVO> deviceScheduleVOS) {
        if (CollUtil.isEmpty(deviceScheduleVOS)) {
            throw new BaseBusinessException(400, "请先选择数据");
        }
        DeviceScheduleVO vo = deviceScheduleVOS.get(0);
        ScheduleTask task = this.getById(vo.getScheduleId());
        if (task.getScheduleStatus() != null && task.getScheduleStatus() == 1) {
            throw new BaseBusinessException(400, "任务已开启,请先停止任务!");
        }
        DeviceInfo deviceInfo = this.deviceInfoService.getById(vo.getDeviceId());
        if (vo.getPrefab() != null && deviceInfo.getDeviceType() != 1) {
            throw new BaseBusinessException(400, deviceInfo.getDeviceName() + "不支持设备预置点");
        }
        if (vo.getChannel() != null && vo.getChannel() > deviceInfo.getChannelNumber()) {
            throw new BaseBusinessException(400, deviceInfo.getDeviceName() + "通道不存在");
        }
        for (DeviceScheduleVO deviceScheduleVO : deviceScheduleVOS) {
            deviceScheduleService.configureSchedule(deviceScheduleVO);
        }
    }

    @Override
    public List<ConfigureDetailVO> configureDetail(String scheduleId, List<String> deviceIds) {
        return deviceScheduleService.getConfigureDetail(scheduleId, deviceIds);
    }

    @Override
    public void startSchedule(String scheduleId) {
        List<DeviceSchedule> deviceSchedules = deviceScheduleService.getByScheduleId(scheduleId);
        if (CollUtil.isEmpty(deviceSchedules)) {
            throw new BaseBusinessException(400, "请先添加设备!");
        }
        ScheduleTask scheduleTask = this.getById(scheduleId);
        if (scheduleTask.getScheduleStatus() != 0) {
            this.stopSchedule(scheduleId);
        }
        String dateTime = scheduleTask.getEndDate() + " 23:59:59";
        if (new Date().getTime() > DateUtil.parseDateTime(dateTime).getTime()) {
            throw new BaseBusinessException(400, "执行任务时间已结束!请重新编辑");
        }
        scheduleTask.setScheduleStatus(1);
        this.updateById(scheduleTask);

        String date = scheduleTask.getStartDate() + " 00:00:00";
        DateTime parseDate = DateUtil.parseDateTime(date);
        if (parseDate.getTime() <= new Date().getTime()) {
            date = DateUtil.formatDateTime(new Date(new Date().getTime() + 1000 * 5));
        }
        String[] dates = date.replace(" ", "-").replace(":", "-").split("-");
        String schedulingPattern = String.format("%s %s %s %s/1 %s ? %s", dates[5], dates[4], dates[3], dates[2], dates[1], dates[0]);
        TaskSchedule taskSchedule = new TaskSchedule(scheduleTask, this, deviceScheduleService, deviceInfoService);
        log.info("首次执行计划:" + scheduleTask + schedulingPattern);
        String schedule = CronUtil.schedule(schedulingPattern, taskSchedule);
        Constants.tempMap.put(scheduleId, schedule);
    }

    @Override
    public void stopSchedule(String scheduleId) {
        this.deviceScheduleService.stopSchedule(scheduleId);
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setScheduleId(scheduleId);
        scheduleTask.setScheduleStatus(ScheduleTask.STATUS_FALSE);
        this.updateById(scheduleTask);
    }

    @Override
    public Boolean deleteConfigureDevice(String scheduleId, List<String> ids) {
        this.deviceScheduleService.remove(new QueryWrapper<DeviceSchedule>().eq(DeviceSchedule.SCHEDULE_ID, scheduleId)
                .in(DeviceSchedule.DEVICE_ID, ids));
        return true;
    }

    @Override
    public int getTaskSum(Integer type) {
        QueryWrapper<ScheduleTask> scheduleTaskQueryWrapper = new QueryWrapper<>();
        scheduleTaskQueryWrapper.eq(ScheduleTask.CREATED_USER, LoginUserUtil.getUserId());
        if (type != null) {
            scheduleTaskQueryWrapper.eq(ScheduleTask.SCHEDULE_STATUS, type);
        }
        return this.count(scheduleTaskQueryWrapper);
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) {
        String filepath = null;
        try {
            File file = MultipartFileUtil.multipartFileToFile(multipartFile);
            filepath = Constants.WATERMARK_PATH + UUID.randomUUID() + file.getName();
            FileUtil.move(file, new File(filepath), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filepath == null) {
            throw new BaseBusinessException(400, "图片解析失败,请重新上传");
        }
        return filepath.replace(Constants.Root_PATH, "");
    }


}
