package com.wt.security.properties;


import com.wt.security.code.FilterOrderCode;
import com.wt.security.server.AuthSecurity;
import com.wt.security.server.AuthorizeSecurity;
import com.wt.security.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({AuthProperties.class})
public class AuthAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AuthAutoConfiguration.class);

    @Resource
    private AuthProperties authProperties;


    @Bean
    public FilterRegistrationBean<SpecialPathFilter> specialPathFilter() {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_100.getName());
        FilterRegistrationBean<SpecialPathFilter> registration = new FilterRegistrationBean<>();
        SpecialPathFilter specialPathFilter = new SpecialPathFilter();
        specialPathFilter.setAuthenticationProperties(authProperties);
        registration.setFilter(specialPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("specialPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_100.getCode());
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ProjectPathFilter> projectPathFilter() {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_200.getName());
        FilterRegistrationBean<ProjectPathFilter> registration = new FilterRegistrationBean<>();
        ProjectPathFilter projectPathFilter = new ProjectPathFilter();
        projectPathFilter.setAuthenticationProperties(authProperties);
        authProperties.getProjectUrl().add(authProperties.getErrorUrl());
        registration.setFilter(projectPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("projectPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_200.getCode());
        return registration;
    }

    @Bean
    public FilterRegistrationBean<DecryptPathFilter> decryptPathFilter() {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_210.getName());
        FilterRegistrationBean<DecryptPathFilter> registration = new FilterRegistrationBean<>();
        DecryptPathFilter decryptPathFilter = new DecryptPathFilter();
        decryptPathFilter.setAuthenticationProperties(authProperties);
        registration.setFilter(decryptPathFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("decryptPathFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_210.getCode());
        return registration;
    }

    @ConditionalOnExpression("${security.auth-enable:false}")
    @ConditionalOnBean(AuthSecurity.class)
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(AuthSecurity authSecurity) {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_300.getName());
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationProperties(authProperties);
        authenticationFilter.setAuthenticationServerSource(authSecurity);
        registration.setFilter(authenticationFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("authenticationFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_300.getCode());
        return registration;
    }

    @ConditionalOnExpression("${security.auth-enable:false}")
    @ConditionalOnBean(AuthSecurity.class)
    @Bean
    public FilterRegistrationBean<TokenLifeFilter> tokenLifeFilter(AuthSecurity authSecurity) {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_310.getName());
        FilterRegistrationBean<TokenLifeFilter> registration = new FilterRegistrationBean<>();
        TokenLifeFilter tokenLifeFilter = new TokenLifeFilter();
        tokenLifeFilter.setAuthenticationProperties(authProperties);
        tokenLifeFilter.setAuthenticationServerSource(authSecurity);
        registration.setFilter(tokenLifeFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("tokenLifeFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_310.getCode());
        return registration;
    }

    @ConditionalOnExpression("${security.auth-enable:false} && ${security.authorize-enable:false}")
    @ConditionalOnBean(AuthorizeSecurity.class)
    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(AuthorizeSecurity authorizeSecurity) {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_400.getName());
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setAuthenticationProperties(authProperties);
        authorizationFilter.setAuthorizationServerSource(authorizeSecurity);
        registration.setFilter(authorizationFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("authorizationFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_400.getCode());
        return registration;
    }

    @ConditionalOnExpression("${security.request-data-enable:false}")
    @Bean
    public FilterRegistrationBean<RequestDataFilter> requestDataFilter() {
        log.info("构建 {}",FilterOrderCode.FILTER_ORDER_CODE_500.getName());
        FilterRegistrationBean<RequestDataFilter> registration = new FilterRegistrationBean<>();
        RequestDataFilter requestDataFilter = new RequestDataFilter();
        requestDataFilter.setAuthenticationProperties(authProperties);
        registration.setFilter(requestDataFilter);
        //配置过滤路径
        registration.addUrlPatterns("/*");
        //设置filter名称
        registration.setName("requestDataFilter");
        //请求中过滤器执行的先后顺序，值越小越先执行
        registration.setOrder(FilterOrderCode.FILTER_ORDER_CODE_500.getCode());
        return registration;
    }

}
