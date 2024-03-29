package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.properties.SecurityProperties;
import com.wt.security.util.PathCheckUtil;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ProjectPathFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ProjectPathFilter.class);
    private SecurityProperties securityProperties;

    public void setAuthenticationProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            List<String> urlFilter = securityProperties.getProjectUrl();
            if(!CollectionUtil.isEmpty(urlFilter)){
                String url = request.getRequestURI();
                PathCheckUtil.pathMatch(urlFilter,url,threadLocalEntity::setProject);
            }
            filterChain.doFilter(request, response);
        } catch(Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),BasicCode.BASIC_CODE_99992.getCode(),e.getMessage());
        }
    }

}
