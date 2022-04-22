package com.tree.clouds.schedule.mapper;

import com.tree.clouds.schedule.model.entity.DeviceSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.vo.ConfigureDetailVO;

import java.util.List;

/**
 * <p>
 * 设备计划中间表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface DeviceScheduleMapper extends BaseMapper<DeviceSchedule> {

    List<ConfigureDetailVO> getConfigureDetail(String scheduleId, List<String> ids);
}
