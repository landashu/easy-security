package com.wt.security.exp.impl;

import com.wt.security.code.BasicCode;
import com.wt.security.exp.IErrorCode;

/**
 * 所属Y-API 模块
 *
 * @Author big uncle
 * @Date 2020/4/8 11:00
 * @module
 **/
public class AuthorizationException extends BasicException {


    public AuthorizationException(String msg){
        super(BasicCode.BASIC_CODE_403.getCode(),msg);
    }

    public AuthorizationException(IErrorCode iErrorCode){
        super(iErrorCode);
    }
}
