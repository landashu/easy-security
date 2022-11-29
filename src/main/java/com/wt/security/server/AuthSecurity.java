package com.wt.security.server;

public interface AuthSecurity {


    Object getAuthUser(String key);


    void renewAuth(String key);

}
