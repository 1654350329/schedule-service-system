package com.tree.clouds.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.schedule.model.entity.ImageInfo;
import com.tree.clouds.schedule.model.vo.ImageInfoVO;
import com.tree.clouds.schedule.model.vo.TreeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 * 图片列表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-04-12
 */
public interface ImageInfoMapper extends BaseMapper<ImageInfo> {

    List<String> getAllYear(String taskId);

    List<ImageInfoVO> getDate(String taskId);

    List<String> getDateYear(String taskId);

    List<String> getDateMonth(String taskId, String year);

    List<String> getDateDay(String taskId, String year, String month);

    List<ImageInfo> getByDate(String taskId, String year, String month, String day);
}
