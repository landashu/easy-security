package com.wt.security.filter;

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

/**
 * @Author big uncle
 * @Date 2019/11/28 17:45
 **/
public class TokenLifeFilter implements Filter {

    private static final Log log = LogFactory.getLog(TokenLifeFilter.class);
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
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                renewAuth(request);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            // 跳转至失败处理器
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),e);
        }
    }


    public void renewAuth(HttpServletRequest httpServletRequest) throws BasicException {
        String token = httpServletRequest.getHeader(authenticationProperties.getToken());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        String accessToken = authenticationProperties.getAuthenticate() + token;
        authSecurity.renewAuth(accessToken);
    }


}
