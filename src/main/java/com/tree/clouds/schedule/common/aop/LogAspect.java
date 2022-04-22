package com.tree.clouds.schedule.common.aop;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.tree.clouds.schedule.model.entity.OperationLog;
import com.tree.clouds.schedule.service.OperationLogService;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Pointcut("@annotation(com.tree.clouds.schedule.common.aop.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        try {
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //当想获得注解里面的属性，可以直接注入改注解
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            saveLog(point, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void saveLog(ProceedingJoinPoint joinPoint, HttpServletRequest request) throws IOException {
        //获取ip地址
        String ipAddresses = request.getRemoteAddr();
        OperationLog operationLog = new OperationLog();
        operationLog.setIp(ipAddresses);
        operationLog.setCreatedUser(LoginUserUtil.getUserId());
        operationLog.setOperation(getLogValue(joinPoint));
        operationLogService.save(operationLog);
    }

    private String getLogValue(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        Log log = method.getAnnotation(Log.class);

        return log.value();
    }

    /**
     * 拦截web层异常，记录异常日志，并返回友好信息到前端
     * 目前只拦截BaseBusinessException，是否要拦截Error需再做考虑
     *
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void handleThrowing(BaseBusinessException e) {
        String errorMsg = StrUtil.isEmpty(e.getMessage()) ? "系统异常" : e.getMessage();
        writeContent(errorMsg);
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void handleThrowing(Exception e) {
        String errorMsg = StrUtil.isEmpty(e.getMessage()) ? "系统异常" : e.getMessage();
        writeContent(errorMsg);
    }

    /**
     * 将内容输出到浏览器
     *
     * @param content 输出内容
     */
    private void writeContent(String content) {
        PrintWriter writer = null;
        try {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            if (response != null && response.isCommitted()) {
                return;
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.setHeader("icop-content-type", "exception");
            response.setHeader("Access-Control-Allow-Origin", "*");
            writer = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", 400);
        map.put("msg", content);
        JSONObject json = new JSONObject(map);
        writer.print(json.toJSONString());
        writer.flush();
        writer.close();
    }
}