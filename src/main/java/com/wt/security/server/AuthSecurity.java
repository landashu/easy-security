package com.wt.security.server;

public interface AuthSecurity {


    Object getAuthUser(String key);


    default void renewAuth(String key){};

}
