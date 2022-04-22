package com.tree.clouds.schedule.security;

import cn.hutool.json.JSONUtil;
import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.model.entity.LoginLog;
import com.tree.clouds.schedule.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private LoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //登入日志
        String ipAddresses = request.getRemoteAddr();
        String username = request.getParameter("username");
        LoginLog loginLog = new LoginLog();
        String errorInfo = exception.getMessage().equals("Bad credentials") ? "用户名或密码错误，请重新输入" : exception.getMessage();
        loginLog.setErrorInfo(errorInfo);
        loginLog.setErrorSort("账号或密码错误");
        loginLog.setIp(ipAddresses);
        loginLog.setAccount(username);
        loginLog.setStatus(2);
        loginLogService.save(loginLog);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        RestResponse result = RestResponse.fail(errorInfo);
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

        outputStream.flush();
        outputStream.close();
    }
}
