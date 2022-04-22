package com.tree.clouds.schedule.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: 林振坤
 * @description:
 * @date: 2021/05/07 16:14
 */
@Component
public class ExcelListener<T> extends AnalysisEventListener {
    private static Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);
    //    private static final int BATCH_COUNT = 10;
    private List<T> datas = new ArrayList<>();

    @Override
    public void invoke(Object data, AnalysisContext context) {
        LOGGER.info("导入数据{}", JSON.toJSONString(data));
        //数据存储到list，供批量处理
        datas.add((T) data);
//        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (datas.size() >= BATCH_COUNT) {
//            // 存储完成清理datas
//              saveData();
//            datas.clear();
//        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！");
        // datas.clear();//解析结束销毁不用的资源
    }
}
