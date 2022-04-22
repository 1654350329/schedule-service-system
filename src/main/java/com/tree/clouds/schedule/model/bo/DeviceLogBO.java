package com.tree.clouds.schedule.model.bo;

import com.tree.clouds.schedule.model.entity.DeviceInfo;
import com.tree.clouds.schedule.model.vo.DeviceLogVO;
import lombok.Data;

import java.util.List;

@Data
public class DeviceLogBO extends DeviceInfo {
    private List<DeviceLogVO> deviceLogVOS;
}
