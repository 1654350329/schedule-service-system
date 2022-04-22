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

    // 定义一个线程域，存放登录用户
    private static final ThreadLocal<UserManage> tl = new ThreadLocal<>();
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserManageService userManageService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public static UserManage getLoginUser() {
        return tl.get();
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
        tl.set(userManage);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userManage.getUserName(), null, userDetailService.getUserAuthority(userManage.getUserId()));

        SecurityContextHolder.getContext().setAuthentication(token);
        //重新签发token
        // 生成jwt，并放置到请求头中
        String netToken = jwtUtils.generateToken(userManage.getAccount());
        response.setHeader(jwtUtils.getHeader(), netToken);
        chain.doFilter(request, response);
    }
    /**
     * 处理token过期情况
     *
     * @param response
     * @param request
     * @param filterChain
     * @return
     * @throws IOException
     */
//    private void handleTokenExpired(HttpServletResponse response, HttpServletRequest request, FilterChain filterChain) throws IOException, ServletException {
//        // 获取刷新 token
//        String refreshTokenHeader = request.getHeader(jwtUtils.getHeader());
//        // 检测 refresh-token 是否是我们系统中签发的
////        if (!checkIsTokenAuthorizationHeader(refreshTokenHeader)) {
////            log.debug("获取到刷新认证头:[{}]的值:[{}]但不是我们系统中登录后签发的。", tokenProperties.getRefreshHeaderName(), refreshTokenHeader);
////            writeJson(response, "token过期了，refresh token 不是我们系统签发的");
////            return;
////        }
//        // 解析 refresh-token
//        Jws<Claims> refreshToken = JwtUtils.parserAuthenticateToken(getRealAuthorizationToken(refreshTokenHeader),
//                tokenProperties.getSecretKey());
//        // 判断 refresh-token 是否不合法
//        if (null == refreshToken) {
//            writeJson(response, "refresh token不合法");
//            return;
//        }
//        // 判断 refresh-token 是否过期
//        if (JwtUtils.isJwtExpired(refreshToken)) {
//            writeJson(response, "refresh token 过期了");
//            return;
//        }
//        // 重新签发 token
//
//        String newToken = JwtUtils.generatorJwtToken(
//                refreshToken.getBody().get(tokenProperties.getUserId()),
//                tokenProperties.getUserId(),
//                tokenProperties.getTokenExpireSecond(),
//                tokenProperties.getSecretKey()
//        );
//        response.addHeader(tokenProperties.getAuthorizationHeaderName(), newToken);
//
//        // 构建认证对象
//        JwtUtils.buildAuthentication(JwtUtils.parserAuthenticateToken(newToken, tokenProperties.getSecretKey()), tokenProperties.getUserId());
//
//        filterChain.doFilter(request, response);
//    }

}
