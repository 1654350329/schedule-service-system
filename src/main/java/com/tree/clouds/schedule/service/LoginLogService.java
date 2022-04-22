package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.LoginLog;
import com.tree.clouds.schedule.model.vo.ExportLoginLogVO;
import com.tree.clouds.schedule.model.vo.LoginLogPageVO;


import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登入日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
public interface LoginLogService extends IService<LoginLog> {

    IPage<LoginLog> loginLogPage(LoginLogPageVO loginLogPageVO);

    void updateLongTime(String userAccount);

}
