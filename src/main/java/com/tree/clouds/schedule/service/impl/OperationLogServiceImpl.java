package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.mapper.OperationLogMapper;
import com.tree.clouds.schedule.model.entity.OperationLog;
import com.tree.clouds.schedule.model.vo.OperationLogPageVO;
import com.tree.clouds.schedule.service.OperationLogService;
import com.tree.clouds.schedule.utils.DownloadFile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public IPage<OperationLog> operationLogPage(OperationLogPageVO operationLogPageVO) {
        IPage<OperationLog> page = operationLogPageVO.getPage();
        return this.baseMapper.operationLogPage(page, operationLogPageVO);
    }

    @Override
    public void exportOperationLog(OperationLogPageVO operationLogPageVO, HttpServletResponse response) {
        List<OperationLog> records = operationLogPage(operationLogPageVO).getRecords();
        String fileName = "操作日志" + DateUtil.formatDate(new Date()) + ".xlsx";
        EasyExcel.write(Constants.TMP_HOME + fileName, OperationLog.class).sheet("操作日志")
                .doWrite(records);
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);
    }
}
