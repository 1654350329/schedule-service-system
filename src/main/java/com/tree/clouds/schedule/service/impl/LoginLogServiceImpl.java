package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.LoginLogMapper;
import com.tree.clouds.schedule.model.entity.LoginLog;
import com.tree.clouds.schedule.model.vo.ExportLoginLogVO;
import com.tree.clouds.schedule.model.vo.LoginLogPageVO;
import com.tree.clouds.schedule.service.LoginLogService;
import com.tree.clouds.schedule.utils.DownloadFile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 登入日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public IPage<LoginLog> loginLogPage(LoginLogPageVO loginLogPageVO) {
        IPage<LoginLog> page = loginLogPageVO.getPage();
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(LoginLog.CREATED_TIME);
        if (StrUtil.isNotBlank(loginLogPageVO.getIp())) {
            wrapper.eq(LoginLog.IP, loginLogPageVO.getIp());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getAccount())) {
            wrapper.eq(LoginLog.ACCOUNT, loginLogPageVO.getAccount());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getLoginStartTime())) {
            wrapper.gt(LoginLog.CREATED_TIME, loginLogPageVO.getLoginStartTime());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getLoginEndTime())) {
            wrapper.lt(LoginLog.CREATED_TIME, loginLogPageVO.getLoginEndTime());
        }
        return this.baseMapper.selectPage(page, wrapper);

    }

    @Override
    public void updateLongTime(String userAccount) {
        try {
            if (userAccount != null) {
                LoginLog loginLog = this.baseMapper.selectOne(new QueryWrapper<LoginLog>()
                        .eq(LoginLog.ACCOUNT, userAccount)
                        .orderByDesc(LoginLog.CREATED_TIME)
                        .last("limit 1"));

                loginLog.setLongTime(loginLog.getCreatedTime() + " - " + DateUtil.now());
                this.baseMapper.updateById(loginLog);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}
