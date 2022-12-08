package com.wt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "security")
public class AuthProperties {

    private boolean authEnable = false;

    private Boolean authorizeEnable = false;

    private String authKey = "SECURITY:AUTHENTICATE:";

    private String authorizeKey = "SECURITY:AUTHORIZE:";

    private String tokenKey = "token";

    private List<String> projectUrl;

    private List<String> specialUrl;

    private List<String> decryptUrl;

    private String errorUrl = "/failure/authenticationFilter";

    private Boolean requestDataEnable = false;

    private String key;

    public boolean isAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(boolean authEnable) {
        this.authEnable = authEnable;
    }

    public Boolean getAuthorizeEnable() {
        return authorizeEnable;
    }

    public void setAuthorizeEnable(Boolean authorizeEnable) {
        this.authorizeEnable = authorizeEnable;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthorizeKey() {
        return authorizeKey;
    }

    public void setAuthorizeKey(String authorizeKey) {
        this.authorizeKey = authorizeKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
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
