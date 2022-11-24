package com.wt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthenticationProperties;
import com.wt.security.util.RequestDataWrapper;
import com.wt.security.util.ResponseDataWrapper;
import com.wt.security.util.ThreadLocalUtil;
import org.apache.commons.codec.Charsets;
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
public class DataFilter implements Filter {

    private static final Log log = LogFactory.getLog(DataFilter.class);
    private AuthenticationProperties authenticationProperties;

    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            request.setCharacterEncoding(Charsets.UTF_8.toString());
            response.setCharacterEncoding(Charsets.UTF_8.toString());
            // 不是特殊路径，且开启了使用 RequestData功能
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!threadLocalEntity.getSpecial()
                    && authenticationProperties.getRequestDataEnable()){
                request = new RequestDataWrapper(request, authenticationProperties, mapper);
                ResponseDataWrapper responseDataWrapper = new ResponseDataWrapper(response);
                filterChain.doFilter(request, responseDataWrapper);
                responseDataWrapper.changeContent(response, authenticationProperties, mapper);
                return;
            }
            filterChain.doFilter(request, servletResponse);
        }catch(BasicException basicException){
            log.error(basicException.getMsg());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),basicException);
        }catch(Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),BasicCode.BASIC_CODE_99993.getCode(),e.getMessage());
        }
    }

}
