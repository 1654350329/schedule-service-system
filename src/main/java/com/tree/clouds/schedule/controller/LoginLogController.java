package com.tree.clouds.schedule.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.common.aop.Log;
import com.tree.clouds.schedule.model.entity.LoginLog;
import com.tree.clouds.schedule.model.vo.LoginLogPageVO;
import com.tree.clouds.schedule.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登入日志 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@RestController
@RequestMapping("/login-log")
@Api(value = "login-log", tags = "登入日志模块")
public class LoginLogController {
    @Autowired
    private LoginLogService loginLogService;

    @PostMapping("/loginLogPage")
    @ApiOperation(value = "登入日志分页查询")
    @Log("登入日志分页查询")
    @PreAuthorize("hasAuthority('login:log:list')")
    public RestResponse<IPage<LoginLog>> loginLogPage(@RequestBody LoginLogPageVO loginLogPageVO) {
        IPage<LoginLog> page = loginLogService.loginLogPage(loginLogPageVO);
        return RestResponse.ok(page);
    }
}

