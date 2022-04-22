package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.bo.DeviceLogBO;
import com.tree.clouds.schedule.model.entity.DeviceLog;

import java.util.List;

/**
 * <p>
 * 设备日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-04-06
 */
public interface DeviceLogService extends IService<DeviceLog> {

    void saveLog(String deviceId, Integer errorCode, boolean status);

    List<DeviceLog> deviceLog(String id);

}
