package com.tree.clouds.schedule.security;

import cn.hutool.core.util.StrUtil;
import com.tree.clouds.schedule.model.entity.UserManage;
import com.tree.clouds.schedule.service.UserManageService;
import com.tree.clouds.schedule.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserManageService userManageService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token已过期");
        }

        String account = claim.getSubject();
        // 获取用户的权限等信息
        UserManage userManage = userManageService.getUserByAccount(account);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userManage.getUserName(), userManage, userDetailService.getUserAuthority(userManage.getUserId()));

        SecurityContextHolder.getContext().setAuthentication(token);
        //重新签发token
        // 生成jwt，并放置到请求头中
        String netToken = jwtUtils.generateToken(userManage.getAccount());
        response.setHeader(jwtUtils.getHeader(), netToken);
        chain.doFilter(request, response);
    }

}
