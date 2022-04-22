package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.mapper.SysMenuMapper;
import com.tree.clouds.schedule.mapper.SysRoleMenuMapper;
import com.tree.clouds.schedule.model.bo.SysMenuDto;
import com.tree.clouds.schedule.model.entity.SysMenu;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.model.vo.SysMenuTreeVO;
import com.tree.clouds.schedule.service.SysMenuService;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 我的公众号：MarkerHub
 * @since 2021-04-05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private UserManageService sysUserService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        UserManage sysUser = sysUserService.getUserByAccount(LoginUserUtil.getUserAccount());
        List<String> menuIds = sysRoleMenuMapper.getNavMenuIds(sysUser.getUserId());
        List<SysMenu> menus = this.listByIds(menuIds).stream().sorted(Comparator.comparing(SysMenu::getOrderNum)).collect(Collectors.toList());
        ArrayList<SysMenuTreeVO> sysMenuVOS = new ArrayList<>();
        for (SysMenu sysMenu : menus) {
            SysMenuTreeVO sysMenuVO = BeanUtil.toBean(sysMenu, SysMenuTreeVO.class);
            sysMenuVOS.add(sysMenuVO);
        }
        // 转树状结构
        List<SysMenuTreeVO> menuTree = buildTreeMenu(sysMenuVOS);

        // 实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<SysMenuTreeVO> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list();
        ArrayList<SysMenuTreeVO> sysMenuVOS = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            SysMenuTreeVO sysMenuVO = BeanUtil.toBean(sysMenu, SysMenuTreeVO.class);
            sysMenuVOS.add(sysMenuVO);
        }
        // 转成树状结构
        return buildTreeMenu(sysMenuVOS);
    }

    @Override
    public List<String> getRole(String id) {
        return this.baseMapper.getMenuIdByRoleId(id);
    }

    private List<SysMenuDto> convert(List<SysMenuTreeVO> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();

            dto.setId(m.getId());
            dto.setTitle(m.getName());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<SysMenuTreeVO> buildTreeMenu(List<SysMenuTreeVO> menus) {

        List<SysMenuTreeVO> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysMenuTreeVO menu : menus) {

            for (SysMenuTreeVO e : menus) {
                if (menu.getId().equals(e.getParentId())) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId().equals("0")) {
                finalMenus.add(menu);
            }
        }

        return finalMenus;
    }
}
