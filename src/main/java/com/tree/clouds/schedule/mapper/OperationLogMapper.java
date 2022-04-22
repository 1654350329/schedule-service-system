package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.model.entity.OperationLog;
import com.tree.clouds.schedule.model.vo.OperationLogPageVO;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 操作日志 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    IPage<OperationLog> operationLogPage(IPage<OperationLog> page, @Param("operationLogPageVO") OperationLogPageVO operationLogPageVO);
}
