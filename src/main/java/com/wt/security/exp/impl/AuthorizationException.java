package com.wt.security.exp.impl;

import com.wt.security.code.BasicCode;
import com.wt.security.exp.IErrorCode;


public class AuthorizationException extends BasicException {


    public AuthorizationException(String msg){
        super(BasicCode.BASIC_CODE_403.getCode(),msg);
    }

    public AuthorizationException(IErrorCode iErrorCode){
        super(iErrorCode);
    }
}
