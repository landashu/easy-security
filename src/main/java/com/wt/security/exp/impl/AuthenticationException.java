package com.wt.security.exp.impl;

import com.wt.security.code.BasicCode;
import com.wt.security.exp.IErrorCode;


public class AuthenticationException extends BasicException {

    public AuthenticationException(String msg){
        super(BasicCode.BASIC_CODE_401.getCode(),msg);
    }

    public AuthenticationException(IErrorCode iErrorCode){
        super(iErrorCode);
    }

}
