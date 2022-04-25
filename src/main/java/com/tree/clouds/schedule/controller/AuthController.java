package com.tree.clouds.schedule.controller;


import com.tree.clouds.schedule.common.RestResponse;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Api(value = "auth", tags = "用户登入模块")
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserManageService userManageService;


    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    private RestResponse<Map> getInfo() {
        //roles, name, avatar, introduction
        UserManage user = userManageService.getById(LoginUserUtil.getUserId());
        String userAuthorityInfo = userManageService.getUserAuthorityInfo(user.getUserId());
        List<String> roles = Arrays.stream(userAuthorityInfo.split(",")).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("roles", roles);
        map.put("name", user.getUserName());
        map.put("userId", user.getUserId());

        return RestResponse.ok(map);
    }

    // 普通用户、超级管理员
//    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/pass")
    @ApiOperation(value = "密码加密")
    public RestResponse<String> pass() {

        // 加密后密码
        String password = bCryptPasswordEncoder.encode("111111");

        boolean matches = bCryptPasswordEncoder.matches("111111", password);

        System.out.println("匹配结果：" + matches);

        return RestResponse.ok(password);
    }

}
