package com.wt.security.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.AuthenticationException;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.SecurityProperties;
import com.wt.security.server.EasySecurityServer;
import com.wt.security.util.IPUtil;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BlacklistFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(BlacklistFilter.class);
    private SecurityProperties securityProperties;

    public void setAuthenticationProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String ip = IPUtil.getIp(request);
            log.debug("Accessing the user's IP: {}",ip);
            if(securityProperties.getBlackList().contains(ip)){
                throw new BasicException(BasicCode.BASIC_CODE_99984);
            }
            filterChain.doFilter(request, response);
        } catch (BasicException e) {
            // 跳转至失败处理器
            log.error(e.getMsg());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),e);
        }
    }


}
