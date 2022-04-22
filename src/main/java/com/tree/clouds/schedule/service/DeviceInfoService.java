package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.vo.DeviceInfoPageVO;
import com.tree.clouds.schedule.model.vo.DeviceInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备信息 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface DeviceInfoService extends IService<DeviceInfo> {

    IPage<DeviceInfo> deviceInfoPage(DeviceInfoPageVO deviceInfoPageVO);

    Boolean deleteDevice(List<String> ids);

    void updateDevice(DeviceInfoVO deviceInfoVO);

    void addDevice(DeviceInfoVO deviceInfoVO);

    void importDevice(MultipartFile file);

    List<Map<String, Object>> getTypePercentage();

    /***
     * 设备总数
     * @return
     */
    Integer getDeviceSum();

    List<DeviceInfo> getList(int type);
}
