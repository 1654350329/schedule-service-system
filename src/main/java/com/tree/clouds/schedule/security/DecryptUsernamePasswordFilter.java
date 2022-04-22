package com.tree.clouds.schedule.security;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.tree.clouds.schedule.utils.BaseBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解密用户名和密码的过滤器
 */
@Component
public class DecryptUsernamePasswordFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecryptUsernamePasswordFilter.class);

    private final RequestMatcher requiresRequestMatcher;

    public DecryptUsernamePasswordFilter() {
        requiresRequestMatcher = new AntPathRequestMatcher("/login", "POST");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresRequestMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }


        chain.doFilter(new DecryptUsernamePasswordHttpServletRequestWrapper(request), res);
    }

    /**
     * 对Form表单POST提交来的用户名密码字段尝试进行RSA解密。
     */
    private static class DecryptUsernamePasswordHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public DecryptUsernamePasswordHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String parameter = super.getParameter(name);
            if (!isDecryptParameter(name)) {
                return parameter;
            }

            try {
                return decrypt(parameter);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        }

        private String decrypt(String str) {
            if (StrUtil.isBlank(str)) {
                return str;
            }

            try {
                //使用base64解密
                return Base64.decodeStr(str);
//                //使用DES加密解密
//                return DESUtil.encryption(str, "123456");
            } catch (Exception e) {
                throw new BaseBusinessException(400, "用户名和密码解密失败");
            }
        }

        private boolean isDecryptParameter(String name) {
            if ("username".equals(name)) {
                return true;
            }

            if ("password".equals(name)) {
                return true;
            }

            return false;
        }
    }
}
