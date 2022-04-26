package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.entity.ImageInfo;
import com.tree.clouds.schedule.model.vo.ImageInfoVO;

import java.util.List;

/**
 * <p>
 * 图片列表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-04-12
 */
public interface ImageInfoService extends IService<ImageInfo> {

    IPage<ImageInfo> dateList(ImageInfoVO imageInfoVO);

    List<ImageInfo> imageInfos(List<String> ids);

    List<String> dateTree(ImageInfoVO imageInfoVO);

    int getImageSum(String taskId);

}
