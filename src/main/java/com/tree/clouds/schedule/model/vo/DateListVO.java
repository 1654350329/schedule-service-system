package com.tree.clouds.schedule.model.vo;

import com.tree.clouds.schedule.model.entity.ImageInfo;
import lombok.Data;

import java.util.List;

@Data
public class DateListVO {
    List<ImageInfo> imageInfos;
    private String date;
}
