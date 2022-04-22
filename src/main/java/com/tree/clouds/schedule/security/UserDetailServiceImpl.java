package com.tree.clouds.schedule.security;


import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserManageService userManageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserManage userManage = userManageService.getUserByAccount(username);
        if (userManage == null) {
            throw new BaseBusinessException(400, "账号不存在");
        }
        if (userManage.getAccountStatus() == 0) {
            throw new BaseBusinessException(400, "账号已停用");
        }
        return new AccountUser(userManage.getUserId(), userManage.getAccount(), userManage.getPassword(), getUserAuthority(userManage.getUserId()));
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     *
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(String userId) {

        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority = userManageService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
