package com.wt.security.filter;

import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.AuthenticationException;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthProperties;
import com.wt.security.server.AuthSecurity;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenLifeFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(TokenLifeFilter.class);
    private AuthProperties authProperties;
    private AuthSecurity authSecurity;

    public AuthProperties getAuthenticationProperties() {
        return authProperties;
    }

    public void setAuthenticationProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public AuthSecurity getAuthenticationServerSource() {
        return authSecurity;
    }

    public void setAuthenticationServerSource(AuthSecurity authSecurity) {
        this.authSecurity = authSecurity;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                renewAuth(request);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            // 跳转至失败处理器
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, authProperties.getErrorUrl(),e);
        }
    }


    public void renewAuth(HttpServletRequest httpServletRequest) throws BasicException {
        String token = httpServletRequest.getHeader(authProperties.getTokenKey());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        authSecurity.renewAuth(token);
    }


}
