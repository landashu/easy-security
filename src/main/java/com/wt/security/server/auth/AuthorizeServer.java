package com.wt.security.server.auth;

import java.util.List;

public interface AuthorizeServer {

    Object getAuthUser(String token);

    List<String> getAuthorizeUrl(String token);

}
