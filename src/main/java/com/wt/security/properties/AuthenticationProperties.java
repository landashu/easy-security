package com.wt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "security")
public class AuthenticationProperties {

    private boolean enable = false;

    private String authenticate = "SECURITY:AUTHENTICATE:";

    private String authorize = "SECURITY:AUTHORIZE:";

    private String token = "token";

    private List<String> projectUrl;

    private List<String> specialUrl;

    private List<String> decryptUrl;

    private String errorUrl = "/failure/authenticationFilter";

    private Boolean authorityLock = false;

    private Boolean requestDataEnable = false;

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
