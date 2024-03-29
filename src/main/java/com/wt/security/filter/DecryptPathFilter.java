package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
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


public class DecryptPathFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(DecryptPathFilter.class);
    private SecurityProperties securityProperties;

    public void setAuthenticationProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            List<String> urlFilter = securityProperties.getDecryptUrl();
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            // 为空则不拦截
            if(!CollectionUtil.isEmpty(urlFilter)){
                if(StrUtil.isEmpty(securityProperties.getSecretKey())){
                    throw new BasicException(BasicCode.BASIC_CODE_99990);
                }
                String url = request.getRequestURI();
                PathCheckUtil.pathMatch(urlFilter,url,threadLocalEntity::setDecrypt);
            }
            filterChain.doFilter(request, response);
        } catch(BasicException e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),e);
        }catch (Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl()
                    ,BasicCode.BASIC_CODE_99994.getCode(),e.getMessage());
        }
    }

}
