package com.wt.security.server;

import java.util.List;

public interface AuthorizeSecurity {

    /**
     * 获取权限
     * @author big uncle
     * @date 2020/7/23 16:55
     * @param key
     * @return java.util.List<java.lang.String>
    **/
    List<String> getAuthorizeUrl(String key);


}
