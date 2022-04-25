package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.hkws.MonitorCameraInfo;
import com.tree.clouds.schedule.common.hkws.TestHikvision;
import com.tree.clouds.schedule.listener.ExcelListener;
import com.tree.clouds.schedule.mapper.DeviceInfoMapper;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.tree.clouds.schedule.model.vo.DeviceInfoPageVO;
import com.tree.clouds.schedule.model.vo.DeviceInfoVO;
import com.tree.clouds.schedule.service.AlbumRecordService;
import com.tree.clouds.schedule.service.DeviceInfoService;
import com.tree.clouds.schedule.service.DeviceScheduleService;
import com.tree.clouds.schedule.service.ImageInfoService;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import com.tree.clouds.schedule.utils.MultipartFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备信息 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
@Service
@Slf4j
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements DeviceInfoService {

    @Autowired
    private DeviceScheduleService deviceScheduleService;
    @Autowired
    private ImageInfoService imageInfoService;
    @Autowired
    private AlbumRecordService albumRecordService;

    @Override
    public IPage<DeviceInfo> deviceInfoPage(DeviceInfoPageVO deviceInfoPageVO) {
        IPage<DeviceInfo> page = deviceInfoPageVO.getPage();
        page = this.baseMapper.deviceInfoPage(page, deviceInfoPageVO, LoginUserUtil.getUserId());
        if (deviceInfoPageVO.getScheduleId() != null) {
            for (DeviceInfo record : page.getRecords()) {
                DeviceSchedule deviceSchedule = this.deviceScheduleService.getByScheduleIdAndDeviceId(deviceInfoPageVO.getScheduleId(), record.getDeviceId());
                record.setImageNumber(this.imageInfoService.getImageSum(deviceSchedule.getTaskId()));
                record.setVideoNumber(this.albumRecordService.getRecordSum(deviceSchedule.getTaskId()));
            }
        }

        return page;
    }

    @Override
    public Boolean deleteDevice(List<String> ids) {
        deviceScheduleService.remove(new QueryWrapper<DeviceSchedule>().in(DeviceSchedule.DEVICE_ID, ids));
        List<DeviceInfo> collect = ids.stream().map(id -> {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceId(id);
            deviceInfo.setDel(1);
            return deviceInfo;
        }).collect(Collectors.toList());
        return this.removeByIds(collect);
    }

    @Override
    public void updateDevice(DeviceInfoVO deviceInfoVO) {
        DeviceInfo deviceInfo = BeanUtil.toBean(deviceInfoVO, DeviceInfo.class);
        this.updateById(deviceInfo);
    }

    @Override
    public void addDevice(DeviceInfoVO deviceInfoVO) {
        MonitorCameraInfo cameraInfo = new MonitorCameraInfo();
        cameraInfo.setCameraIp(deviceInfoVO.getDeviceAddress());
        cameraInfo.setCameraPort(deviceInfoVO.getPort());
        cameraInfo.setUserName(deviceInfoVO.getDeviceAccount());
        cameraInfo.setUserPwd(deviceInfoVO.getDevicePassword());
        MonitorCameraInfo dvrConfig = TestHikvision.getDVRConfig(cameraInfo);
        if (!dvrConfig.getStatus()) {
            System.out.println("设备:" + deviceInfoVO.getDeviceName() + "错误代码: " + dvrConfig.getErrorCode() + "错误信息: " + dvrConfig.getErrorMessage());
            throw new BaseBusinessException(400, "设备:" + deviceInfoVO.getDeviceName() + "错误代码: " + dvrConfig.getErrorCode() + "错误信息: " + dvrConfig.getErrorMessage());
        }
        DeviceInfo deviceInfo = BeanUtil.toBean(deviceInfoVO, DeviceInfo.class);
        deviceInfo.setChannelNumber(cameraInfo.getChannelNumber() == null ? 1 : cameraInfo.getChannelNumber());
        DeviceInfo info = getByIpAndPort(deviceInfo.getDeviceAddress(), deviceInfo.getPort());
        if (info != null) {
            String id = info.getDeviceId();
            info = deviceInfo;
            info.setDeviceId(id);
            this.updateById(info);
        } else {
            this.save(deviceInfo);
        }
    }

    @Override
    @Transactional
    public void importDevice(MultipartFile multipartFile) {
        ExcelListener<DeviceInfoVO> excelListener = new ExcelListener<DeviceInfoVO>();
        try {
            File file = MultipartFileUtil.multipartFileToFile(multipartFile);
            EasyExcel.read(file, DeviceInfoVO.class, excelListener).sheet(0).doRead();
        } catch (Exception e) {
            log.error("设备信息导入异常是:{}", e.getMessage());
            log.error("异常栈：", e);
            throw new BaseBusinessException(500, "设备信息导入异常");
        }
        //获取数据
        List<DeviceInfoVO> deviceInfos = excelListener.getDatas();
        for (DeviceInfoVO deviceInfo : deviceInfos) {
            addDevice(deviceInfo);
        }

    }

    @Override
    public List<Map<String, Object>> getTypePercentage() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int typeOne = this.baseMapper.getTypeNumber(i, LoginUserUtil.getUserId());
            Map<String, Object> map = new LinkedHashMap<>();
            if (i == 0) {
                map.put("name", "枪机");
            }
            if (i == 1) {
                map.put("name", "球机");
            }
            if (i == 2) {
                map.put("name", "全景机");
            }
            map.put("value", typeOne);
            list.add(map);
        }
        return list;
    }

    @Override
    public Integer getDeviceSum() {
        return this.count(new QueryWrapper<DeviceInfo>().eq(DeviceInfo.CREATED_USER, LoginUserUtil.getUserId()));
    }

    @Override
    public List<DeviceInfo> getList(int type) {
        List<DeviceInfo> list = this.list(new QueryWrapper<DeviceInfo>().eq(DeviceInfo.DEVICE_STATUS, type)
                .eq(DeviceInfo.CREATED_USER, LoginUserUtil.getUserId()));
        return list;
    }

    public DeviceInfo getByIpAndPort(String ip, Integer port) {
        QueryWrapper<DeviceInfo> queryWrapper = new QueryWrapper<DeviceInfo>().eq(DeviceInfo.DEVICE_ADDRESS, ip)
                .eq(DeviceInfo.PORT, port);
        return this.getOne(queryWrapper);
    }
}
