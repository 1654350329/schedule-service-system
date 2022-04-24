package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.vo.DeviceInfoPageVO;

/**
 * <p>
 * 设备信息 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    IPage<DeviceInfo> deviceInfoPage(IPage<DeviceInfo> page, DeviceInfoPageVO deviceInfoPageVO, String userId);

    int getTypeNumber(int type, String userId);
}
