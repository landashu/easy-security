package com.wt.security.util;

import com.wt.security.yapi.YApiRule;

public class RequestData<T,U> {

    @YApiRule(required = true)
    private T data;

    @YApiRule(hide = true)
    private U User;

    @YApiRule(hide = true)
    private String token;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public U getUser() {
        return User;
    }

    public void setUser(U user) {
        User = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
