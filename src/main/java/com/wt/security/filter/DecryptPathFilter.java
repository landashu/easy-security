package com.wt.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
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
public class DecryptPathFilter implements Filter {

    private static final Log log = LogFactory.getLog(DecryptPathFilter.class);
    private AuthenticationProperties authenticationProperties;

    public AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public void setAuthenticationProperties(AuthenticationProperties authenticationProperties) {
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 判断是否解密
    **/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            List<String> urlFilter = authenticationProperties.getDecryptUrl();
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            // 为空则不拦截
            if(!CollectionUtil.isEmpty(urlFilter)){
                if(StrUtil.isEmpty(authenticationProperties.getKey())){
                    throw new BasicException(BasicCode.BASIC_CODE_99990);
                }
                String url = request.getRequestURI();
                PathRuleCheck.pathMatch(urlFilter,url,threadLocalEntity::setDecrypt);
            }
            filterChain.doFilter(request, response);
        } catch(BasicException e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl(),e);
        }catch (Exception e){
            log.error(e.getMessage());
            ThreadLocalUtil.forward(request,response,authenticationProperties.getErrorUrl()
                    ,BasicCode.BASIC_CODE_99994.getCode(),e.getMessage());
        }
    }

}
