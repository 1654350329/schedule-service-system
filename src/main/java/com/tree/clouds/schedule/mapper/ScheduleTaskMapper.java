package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.bo.ScheduleTaskBO;
import com.tree.clouds.schedule.model.entity.ScheduleTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.vo.ScheduleTaskPageVO;

/**
 * <p>
 * 计划配置 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-03-14
 */
public interface ScheduleTaskMapper extends BaseMapper<ScheduleTask> {

    IPage<ScheduleTaskBO> schedulePage(IPage<ScheduleTaskBO> page, ScheduleTaskPageVO scheduleTaskPageVO);
}
