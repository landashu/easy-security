package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.properties.AuthenticationProperties;
import com.wt.security.util.PathRuleCheck;
import com.wt.security.util.ThreadLocalUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author big uncle
 * @Date 2019/11/28 17:45
 **/
public class ProjectPathFilter implements Filter {

    private static final Log log = LogFactory.getLog(ProjectPathFilter.class);
    private AuthenticationProperties authenticationProperties;

    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 不拦截 project
    **/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            List<String> urlFilter = authenticationProperties.getProjectUrl();
            if(!CollectionUtil.isEmpty(urlFilter)){
                String url = request.getRequestURI();
                PathRuleCheck.pathMatch(urlFilter,url,threadLocalEntity::setProject);
            }
            filterChain.doFilter(request, response);
        } catch(Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),BasicCode.BASIC_CODE_99992.getCode(),e.getMessage());
        }
    }

}
