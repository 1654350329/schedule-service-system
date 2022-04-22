package com.tree.clouds.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.schedule.model.bo.SysMenuDto;
import com.tree.clouds.schedule.model.entity.SysMenu;
import com.tree.clouds.schedule.model.vo.SysMenuTreeVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentUserNav();

    List<SysMenuTreeVO> tree();

    List<String> getRole(String id);
}
