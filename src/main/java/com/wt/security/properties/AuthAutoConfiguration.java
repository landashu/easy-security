package com.wt.security.properties;


import com.wt.security.server.AuthSecurity;
import com.wt.security.server.AuthorizeSecurity;
import com.wt.security.filter.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@ConditionalOnExpression("${security.enable:false}")
@EnableConfigurationProperties({AuthenticationProperties.class})
public class AuthAutoConfiguration {

    private static final Log log = LogFactory.getLog(AuthAutoConfiguration.class);

    @Resource
    private AuthenticationProperties authenticationProperties;
    @Resource
    AuthSecurity authSecurity;
    @Resource
    AuthorizeSecurity authorizeSecurity;

    @Bean
    public FilterRegistrationBean<SpecialPathFilter> specialPathFilter() {
        FilterRegistrationBean<SpecialPathFilter> registration = new FilterRegistrationBean<>();
        SpecialPathFilter specialPathFilter = new SpecialPathFilter();
        specialPathFilter.setAuthenticationProperties(authenticationProperties);
        registration.setFilter(specialPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("specialPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(100);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ProjectPathFilter> projectPathFilter() {
        FilterRegistrationBean<ProjectPathFilter> registration = new FilterRegistrationBean<>();
        ProjectPathFilter projectPathFilter = new ProjectPathFilter();
        projectPathFilter.setAuthenticationProperties(authenticationProperties);
        authenticationProperties.getProjectUrl().add(authenticationProperties.getErrorUrl());
        registration.setFilter(projectPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("projectPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(200);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<DecryptPathFilter> decryptPathFilter() {
        FilterRegistrationBean<DecryptPathFilter> registration = new FilterRegistrationBean<>();
        DecryptPathFilter decryptPathFilter = new DecryptPathFilter();
        decryptPathFilter.setAuthenticationProperties(authenticationProperties);
        registration.setFilter(decryptPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("decryptPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(210);
        return registration;
    }

    @ConditionalOnClass(AuthSecurity.class)
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationProperties(authenticationProperties);
        authenticationFilter.setAuthenticationServerSource(authSecurity);
        registration.setFilter(authenticationFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("authenticationFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(300);
//        AesEncryptUtil.setKEY(authenticationProperties.getEncryptionKey());
        return registration;
    }

    @ConditionalOnClass(AuthSecurity.class)
    @Bean
    public FilterRegistrationBean<TokenLifeFilter> continueLifeFilter() {
        FilterRegistrationBean<TokenLifeFilter> registration = new FilterRegistrationBean<>();
        TokenLifeFilter tokenLifeFilter = new TokenLifeFilter();
        tokenLifeFilter.setAuthenticationProperties(authenticationProperties);
        tokenLifeFilter.setAuthenticationServerSource(authSecurity);
        registration.setFilter(tokenLifeFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("continueLifeFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(310);
        return registration;
    }

    @ConditionalOnClass(AuthorizeSecurity.class)
    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setAuthenticationProperties(authenticationProperties);
        authorizationFilter.setAuthorizationServerSource(authorizeSecurity);
        registration.setFilter(authorizationFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("authorizationFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(400);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<DataFilter> requestDataFilter() {
        FilterRegistrationBean<DataFilter> registration = new FilterRegistrationBean<>();
        DataFilter dataFilter = new DataFilter();
        dataFilter.setAuthenticationProperties(authenticationProperties);
        registration.setFilter(dataFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("requestDataFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(500);
        return registration;
    }

}
