package com.tree.clouds.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.DeviceLogMapper;
import com.tree.clouds.schedule.model.bo.DeviceLogBO;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.entity.DeviceLog;
import com.tree.clouds.schedule.service.DeviceInfoService;
import com.tree.clouds.schedule.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.ConstantCallSite;
import java.util.List;

/**
 * <p>
 * 设备日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-04-06
 */
@Service
public class DeviceLogServiceImpl extends ServiceImpl<DeviceLogMapper, DeviceLog> implements DeviceLogService {

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Override
    public void saveLog(String deviceId, Integer errorCode, boolean status) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setDeviceStatus(status ? 1 : 0);
        deviceInfoService.updateById(deviceInfo);

        DeviceLog deviceLog = new DeviceLog();
        deviceLog.setDeviceId(deviceId);
        if (errorCode != null && Constants.errorMap.containsKey(errorCode)) {
            deviceLog.setLogInfo(Constants.errorMap.get(errorCode));
        }
        deviceLog.setErrorCode(errorCode);
        this.save(deviceLog);
    }

    @Override
    public List<DeviceLog> deviceLog(String id) {
        return this.list(new QueryWrapper<DeviceLog>().eq(DeviceLog.DEVICE_ID, id).orderByDesc(DeviceLog.CREATED_TIME));
    }

}
