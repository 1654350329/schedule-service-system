package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.OperationLog;
import com.tree.clouds.schedule.model.vo.OperationLogPageVO;
import com.tree.clouds.schedule.service.OperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 操作日志 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@RestController
@RequestMapping("/operation-log")
@Api(value = "operation-log", tags = "操作日志模块")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    @PostMapping("/operationLogPage")
    @ApiOperation(value = "操作日志分页查询")
    @Log("操作日志分页查询")
    @PreAuthorize("hasAuthority('operation:log:list')")
    public RestResponse<IPage<OperationLog>> operationLogPage(@RequestBody OperationLogPageVO operationLogPageVO) {
        IPage<OperationLog> page = operationLogService.operationLogPage(operationLogPageVO);
        return RestResponse.ok(page);
    }

    @GetMapping("/exportOperationLog")
    @ApiOperation(value = "操作日志导出")
    @Log("操作日志导出")
    public void exportOperationLog(OperationLogPageVO operationLogPageVO, HttpServletResponse response) {
        operationLogService.exportOperationLog(operationLogPageVO, response);
    }
}

