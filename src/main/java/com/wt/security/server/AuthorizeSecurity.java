package com.wt.security.server;

import java.util.List;

public interface AuthorizeSecurity {

    List<String> getAuthorizeUrl(String key);


}
