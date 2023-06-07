package com.wt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.SecurityProperties;
import com.wt.security.server.EasySecurityServer;
import com.wt.security.server.wrapper.RequestDataWrapper;
import com.wt.security.server.wrapper.ResponseDataWrapper;
import com.wt.security.util.ThreadLocalUtil;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RequestDataFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestDataFilter.class);
    private SecurityProperties securityProperties;
    public void setAuthenticationProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            request.setCharacterEncoding(Charsets.UTF_8.toString());
            response.setCharacterEncoding(Charsets.UTF_8.toString());
            // 不是特殊路径，且开启了使用 RequestData功能
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial()){
                request = new RequestDataWrapper(request, securityProperties);
                ResponseDataWrapper responseDataWrapper = new ResponseDataWrapper(response,securityProperties);
                filterChain.doFilter(request, responseDataWrapper);
                responseDataWrapper.changeContent();
                return;
            }
            filterChain.doFilter(request, servletResponse);
        }catch(BasicException basicException){
            log.error(basicException.getMsg());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),basicException);
        }catch(Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response, securityProperties.getErrorUrl(),BasicCode.BASIC_CODE_99993.getCode(),e.getMessage());
        }
    }

}
