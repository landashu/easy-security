package com.wt.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.AuthenticationException;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthenticationProperties;
import com.wt.security.server.AuthSecurity;
import com.wt.security.util.ThreadLocalUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationFilter implements Filter {

    private static final Log log = LogFactory.getLog(AuthenticationFilter.class);

    private AuthenticationProperties authenticationProperties;
    private AuthSecurity authSecurity;

    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
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
            // 不为特殊路径 和 项目路径 才获取用户信息
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                Object obj = getUser(request);
                threadLocalEntity.setUser(obj);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            // 跳转至失败处理器
            log.error(e.getMsg());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),e);
        }
    }


    public Object getUser(HttpServletRequest httpServletRequest) throws BasicException {
        String token = httpServletRequest.getHeader(authenticationProperties.getToken());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        Object obj = null;
        String accessToken = authenticationProperties.getAuthenticate() + token;
        obj = httpServletRequest.getSession().getAttribute(accessToken);
        if(ObjectUtil.isEmpty(obj)){
            obj = authSecurity.getAuthUser(accessToken);
        }
        if(ObjectUtil.isEmpty(obj)){
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        return obj;
    }

}
