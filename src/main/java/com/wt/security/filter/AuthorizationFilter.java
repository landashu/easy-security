package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.AuthenticationException;
import com.wt.security.exp.impl.AuthorizationException;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthenticationProperties;
import com.wt.security.server.AuthorizeSecurity;
import com.wt.security.util.ThreadLocalUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class AuthorizationFilter implements Filter {

    private static final Log log = LogFactory.getLog(AuthorizationFilter.class);

    private AuthorizeSecurity authorizeSecurity;
    private AuthenticationProperties authenticationProperties;

    public AuthorizeSecurity getAuthorizationServerSource() {
        return authorizeSecurity;
    }

    public void setAuthorizationServerSource(AuthorizeSecurity authorizeSecurity) {
        this.authorizeSecurity = authorizeSecurity;
    }

    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial() && !threadLocalEntity.getProject()){
                if(!authenticationProperties.getAuthorityLock()){
                    filterChain.doFilter(request, response);
                    return;
                }
                authorize(request);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            log.error(e.getMsg());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),e);
        }
    }

    private void authorize(HttpServletRequest request) throws BasicException {
        String token = request.getHeader(authenticationProperties.getToken());
        if (StrUtil.isEmpty(token)) {
            throw new AuthenticationException(BasicCode.BASIC_CODE_401);
        }
        String accessToken = authenticationProperties.getAuthorize()+token;
        List<String> list = null;
        Object obj = request.getSession().getAttribute(accessToken);
        if(ObjectUtil.isEmpty(obj)){
            list = authorizeSecurity.getAuthorizeUrl(accessToken);
        } else {
            list = (List<String>)obj;
        }
        if(CollectionUtil.isEmpty(list)){
            throw new AuthorizationException(BasicCode.BASIC_CODE_403);
        }
        String url = request.getRequestURI();
        if(!list.contains(url)){
            throw new AuthorizationException(BasicCode.BASIC_CODE_403);
        }
    }

}
