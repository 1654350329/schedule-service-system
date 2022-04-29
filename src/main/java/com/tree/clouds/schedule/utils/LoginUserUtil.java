package com.tree.clouds.schedule.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tree.clouds.schedule.model.entity.UserManage;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 林振坤
 * @description
 * @date 2022/1/2 0002 18:32
 */
public class LoginUserUtil {

    public static String getUserId() {
        try {
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
                return null;
            }
            UserManage UserManage = (UserManage) credentials;
            return UserManage.getUserId();
        } catch (RuntimeException e) {
            return null;
        }

    }


    public static String getUserName() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        UserManage UserManage = (UserManage) credentials;
        return UserManage.getUserName();

    }

    public static String getUserAccount() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        UserManage UserManage = (UserManage) credentials;
        return UserManage.getAccount();
    }
}
