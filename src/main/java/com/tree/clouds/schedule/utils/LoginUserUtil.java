package com.tree.clouds.schedule.utils;

import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.security.JwtAuthenticationFilter;

/**
 * @author 林振坤
 * @description
 * @date 2022/1/2 0002 18:32
 */
public class LoginUserUtil {

    public static String getUserId() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getUserId();
        }
    }

    public static String getUserName() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getUserName();
        }
    }

    public static String getUserAccount() {
        UserManage userManage = JwtAuthenticationFilter.getLoginUser();
        if (userManage == null) {
            return null;
        } else {
            return userManage.getAccount();
        }
    }

}
