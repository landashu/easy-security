package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.util.PathCheckUtil;
import com.wt.security.properties.SecurityProperties;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SpecialPathFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SpecialPathFilter.class);
    private SecurityProperties securityProperties;

    public void setAuthenticationProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = new ThreadLocalUtil.ThreadLocalEntity();
            List<String> urlFilter = securityProperties.getSpecialUrl();
            if(!CollectionUtil.isEmpty(urlFilter)){
                String url = request.getRequestURI();
                // 确定有该URL 就需要放到线程变量中
                PathCheckUtil.pathMatch(urlFilter,url, threadLocalEntity::setSpecial);
            }
            ThreadLocalUtil.threadLocal.set(threadLocalEntity);
            filterChain.doFilter(request, response);
        } catch(Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),BasicCode.BASIC_CODE_99991.getCode(),e.getMessage());
        }finally {
            ThreadLocalUtil.threadLocal.remove();
        }
    }


}
