package com.wt.security.server.auth;

import com.wt.security.exp.impl.BasicException;

import java.util.List;

public interface AuthorizeServer {

    Object getAuthUser(String token) throws BasicException;

    List<String> getAuthorizeUrl(String token) throws BasicException;

}
