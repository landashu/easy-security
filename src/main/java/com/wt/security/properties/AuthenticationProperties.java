package com.wt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author big uncle
 * @Date 2019/11/27 9:27
 **/
@ConfigurationProperties(prefix = "security")
public class AuthenticationProperties {
    /**
     * 是否开启安全认证
    **/
    private boolean enable = false;
    /**
     * 用户认证
    **/
    private String authenticate = "SECURITY:AUTHENTICATE:";
    /**
     * 权限认证
     **/
    private String authorize = "SECURITY:AUTHORIZE:";
    /**
     * 拦截请求中 header 要带的参数
    **/
    private String token = "token";
    /**
     * 项目过滤集合, 如登陆注册不拦截，以英文逗号分隔
    **/
    private List<String> projectUrl;
    /**
     * 特殊路径放弃拦截，如三方回调，以英文逗号分隔
     **/
    private List<String> specialUrl;
    /**
     * 需要解密数据的路径
     **/
    private List<String> decryptUrl;
    /**
     * 失败处理器
    **/
    private String errorUrl = "/failure/authenticationFilter";
    /**
     * 是否关闭|开启权限拦截 默认是关闭
     **/
    private Boolean authorityLock = false;
    /**
     * 是否开启框架自带参数形式，默认是关闭
     **/
    private Boolean requestDataEnable = false;
    /**
     * 密钥必须是16位字符串
    **/
    private String key;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(List<String> projectUrl) {
        this.projectUrl = projectUrl;
    }

    public List<String> getSpecialUrl() {
        return specialUrl;
    }

    public void setSpecialUrl(List<String> specialUrl) {
        this.specialUrl = specialUrl;
    }

    public List<String> getDecryptUrl() {
        return decryptUrl;
    }

    public void setDecryptUrl(List<String> decryptUrl) {
        this.decryptUrl = decryptUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public Boolean getAuthorityLock() {
        return authorityLock;
    }

    public void setAuthorityLock(Boolean authorityLock) {
        this.authorityLock = authorityLock;
    }

    public Boolean getRequestDataEnable() {
        return requestDataEnable;
    }

    public void setRequestDataEnable(Boolean requestDataEnable) {
        this.requestDataEnable = requestDataEnable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
