package com.wt.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(prefix = "easy.security")
public class SecurityProperties {

    private boolean authEnable = false;

    private Boolean authorizeEnable = false;

    private String tokenKey = "token";

    private List<String> projectUrl = new ArrayList<>();

    private List<String> specialUrl = new ArrayList<>();

    private List<String> decryptUrl = new ArrayList<>();

    private String errorUrl = "/failure/authenticationFilter";

    private Boolean requestDataEnable = false;

    /**
     * 长度16位的字母数字组合
     */
    private String secretKey;

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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
