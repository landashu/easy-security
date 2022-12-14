package com.wt.security.filter;

import cn.hutool.core.util.ObjectUtil;
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


public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

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
            // 不为特殊路径和项目路径才获取用户信息
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                Object obj = getUser(request);
                threadLocalEntity.setUser(obj);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            // 跳转至失败处理器
            log.error(e.getMsg());
            ThreadLocalUtil.forward(request,response, authProperties.getErrorUrl(),e);
        }
    }


    public Object getUser(HttpServletRequest httpServletRequest) throws BasicException {
        String token = httpServletRequest.getHeader(authProperties.getTokenKey());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        String accessToken = authProperties.getAuthKey() + token;
        Object obj = authSecurity.getAuthUser(accessToken);
        if(ObjectUtil.isEmpty(obj)){
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        return obj;
    }

}
