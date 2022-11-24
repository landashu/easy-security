package com.wt.security.server;

public interface AuthSecurity {

    /**
     * 从 redis 或者 session 读取用户的方式
     * @Author big uncle
     * @Date 2020/3/28 16:54
     * @param key
     * @return java.lang.Object
    **/
    Object getAuthUser(String key);

    /**
     * 给认证过的用户续命
     * @author big uncle
     * @date 2021/1/22 10:19
     * @param key
     * @return void
    **/
    void renewAuth(String key);

}
