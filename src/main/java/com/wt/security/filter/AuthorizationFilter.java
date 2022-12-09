package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.AuthenticationException;
import com.wt.security.exp.impl.AuthorizationException;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthProperties;
import com.wt.security.server.AuthorizeSecurity;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class AuthorizationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private AuthorizeSecurity authorizeSecurity;
    private AuthProperties authProperties;

    public AuthorizeSecurity getAuthorizationServerSource() {
        return authorizeSecurity;
    }

    public void setAuthorizationServerSource(AuthorizeSecurity authorizeSecurity) {
        this.authorizeSecurity = authorizeSecurity;
    }

    public AuthProperties getAuthenticationProperties() {
        return authProperties;
    }

    public void setAuthenticationProperties(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                authorize(request);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            log.error(e.getMsg());
            ThreadLocalUtil.forward(request,response, authProperties.getErrorUrl(),e);
        }
    }

    private void authorize(HttpServletRequest request) throws BasicException {
        String token = request.getHeader(authProperties.getTokenKey());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        List<String> list = authorizeSecurity.getAuthorizeUrl(token);
        if(CollectionUtil.isEmpty(list)){
            throw new AuthorizationException(BasicCode.BASIC_CODE_403);
        }
        String url = request.getRequestURI();
        if(!list.contains(url)){
            throw new AuthorizationException(BasicCode.BASIC_CODE_403);
        }
    }

}
