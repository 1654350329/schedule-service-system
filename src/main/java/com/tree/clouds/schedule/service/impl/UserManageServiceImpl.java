package com.tree.clouds.schedule.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.schedule.common.Constants;
import com.tree.clouds.schedule.listener.ExcelListener;
import com.tree.clouds.schedule.mapper.SysRoleMenuMapper;
import com.tree.clouds.schedule.mapper.UserManageMapper;
import com.tree.clouds.schedule.model.bo.UserManageBO;
import com.tree.clouds.schedule.model.entity.RoleManage;
import com.tree.clouds.schedule.model.entity.SysMenu;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.model.vo.UpdatePasswordVO;
import com.tree.clouds.schedule.model.vo.UserManagePageVO;
import com.tree.clouds.schedule.service.GroupUserService;
import com.tree.clouds.schedule.service.RoleUserService;
import com.tree.clouds.schedule.service.SysMenuService;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户管理 服务实现类
 * </p>
 *
 * @author lzk
 * @since 2021-12-28
 */
@Service
@Slf4j
public class UserManageServiceImpl extends ServiceImpl<UserManageMapper, UserManage> implements UserManageService {

    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public void rebuildPassword(List<String> ids) {
        // 加密后密码
        String password = bCryptPasswordEncoder.encode("888888");

        List<UserManage> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setPassword(password));
        this.updateBatchById(userManages);
    }

    @Override
    public void userStatus(List<String> ids, int status) {
        List<UserManage> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setAccountStatus(status));
        this.updateBatchById(userManages);
    }

    @Override
    public void importUser(MultipartFile multipartFile) {
        ExcelListener<UserManage> excelListener = new ExcelListener<UserManage>();
        try {
            File file = MultipartFileUtil.multipartFileToFile(multipartFile);
            EasyExcel.read(file, UserManage.class, excelListener).sheet(0).doRead();
        } catch (Exception e) {
            log.error("用户信息导入异常是:{}", e.getMessage());
            log.error("异常栈：", e);
            throw new BaseBusinessException(500, "用户信息导入异常");
        }
        //获取数据
        List<UserManage> userManages = excelListener.getDatas();

        for (UserManage userManage : userManages) {
            UserManage userByAccount = this.baseMapper.isExist(userManage.getAccount(), userManage.getPhoneNumber());
            if (userByAccount != null) {
                throw new BaseBusinessException(400, "账号:" + userByAccount.getAccount() + "已存在!!或手机号码:" + userManage.getPhoneNumber() + "已存在");
            }
            this.save(userManage);
        }

    }

    @Override
    public void exportUser(List<String> ids, HttpServletResponse response) {
        List<UserManage> userManages = this.listByIds(ids);
        String fileName = "用户信息.xlsx";
        EasyExcel.write(Constants.TMP_HOME + fileName, UserManage.class).sheet("用户信息")
                .doWrite(userManages);
        byte[] bytes = DownloadFile.File2byte(new File(Constants.TMP_HOME + fileName));
        DownloadFile.downloadFile(bytes, fileName, response, false);

    }

    @Override
    public IPage<UserManageBO> userManagePage(UserManagePageVO userManagePageVO) {
        IPage<UserManageBO> page = userManagePageVO.getPage();
        IPage<UserManageBO> userManageVOIPage = this.baseMapper.userManagePage(page, userManagePageVO);
        List<UserManageBO> records = userManageVOIPage.getRecords();
        for (UserManageBO record : records) {
            List<RoleManage> roleManages = roleUserService.getRoleByUserId(record.getUserId());
            record.setRoleIds(roleManages.stream().map(RoleManage::getRoleId).collect(Collectors.toList()));
            List<String> roleNames = roleManages.stream().map(RoleManage::getRoleName).collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < roleNames.size(); i++) {
                if (i == roleNames.size() - 1) {
                    stringBuilder.append(roleNames.get(i));
                } else {
                    stringBuilder.append(roleNames.get(i)).append("-");
                }
            }
            record.setRoleName(stringBuilder.toString());

        }
        return userManageVOIPage;
    }

    @Override
    public UserManage getUserByAccount(String account) {
        QueryWrapper<UserManage> wrapper = new QueryWrapper<>();
        wrapper.eq(UserManage.ACCOUNT, account);
        return this.getOne(wrapper);
    }

    @Override
    public String getUserAuthorityInfo(String userId) {

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";
        if (redisUtil.hasKey("GrantedAuthority:" + userId)) {
            authority = (String) redisUtil.get("GrantedAuthority:" + userId);
        } else {
            //获取角色
            List<RoleManage> roles = this.roleUserService.getRoleByUserId(userId);
            authority = roles.stream().map(RoleManage::getRoleCode).collect(Collectors.joining(","));

            List<String> menuIds = sysRoleMenuMapper.getNavMenuIds(userId);
            List<SysMenu> menus = this.sysMenuService.listByIds(menuIds);
            String perms = menus.stream().map(SysMenu::getPerms).collect(Collectors.joining(","));
            authority = authority.concat(",").concat(perms);
            redisUtil.set("GrantedAuthority:" + userId, authority, 60 * 60);
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String userId) {
        redisUtil.del("GrantedAuthority:" + userId);
    }

    @Override
    public void addUserManage(UserManageBO userManageBO) {
        UserManage userByAccount = this.getUserByAccount(userManageBO.getAccount());
        if (userByAccount != null) {
            throw new BaseBusinessException(400, "账号已存在,请重新输入!!");
        }
        //手机号码正则匹配
        String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";
        if (!Pattern.matches(REGEX_MOBILE, userManageBO.getPhoneNumber())) {
            throw new BaseBusinessException(400, "手机号码不合法");
        }
        UserManage userManage = BeanUtil.toBean(userManageBO, UserManage.class);
        String password = bCryptPasswordEncoder.encode(Base64.decodeStr(userManage.getPassword()));
        userManage.setPassword(password);
        this.save(userManage);
        //添加分组
        groupUserService.saveGroupUser(userManage.getUserId(), userManageBO.getGroupId());
        //绑定角色
        roleUserService.addRole(userManageBO.getRoleIds(), userManage.getUserId());

    }

    @Override
    public void updateUserManage(UserManageBO userManageBO) {
        if (StrUtil.isBlank(userManageBO.getUserId())) {
            throw new BaseBusinessException(400, "用户id不许为空");
        }
        UserManage userManage = BeanUtil.toBean(userManageBO, UserManage.class);
        if (StrUtil.isNotBlank(userManageBO.getPassword())) {
            userManage.setPassword(bCryptPasswordEncoder.encode(Base64.decodeStr(userManageBO.getPassword())));
        }
        this.updateById(userManage);
        //角色先删后增
        roleUserService.removeRole(userManage.getUserId());
        roleUserService.addRole(userManageBO.getRoleIds(), userManage.getUserId());
    }

    @Override
    public void deleteUserManage(String userId) {
        UserManage userManage = new UserManage();
        userManage.setUserId(userId);
        userManage.setDel(1);
        this.updateById(userManage);
        roleUserService.removeRole(userId);
        groupUserService.removeUserByUserId(userId);
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(String menuId) {
        List<UserManage> sysUsers = this.baseMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(String roleId) {
        List<UserManage> sysUsers = this.baseMapper.listByRoleId(roleId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }

    @Override
    public void updatePassword(UpdatePasswordVO updatePasswordVO) {
        String password = bCryptPasswordEncoder.encode(Base64.decodeStr(updatePasswordVO.getPassword()));
        UserManage user = new UserManage();
        user.setPassword(password);
        user.setUserId(LoginUserUtil.getUserId());
        this.updateById(user);
    }
}
