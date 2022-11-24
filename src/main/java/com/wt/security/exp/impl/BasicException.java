package com.wt.security.exp.impl;

import com.wt.security.exp.IErrorCode;

/**
 * 异常基础类
 * @Author big uncle
 * @Date 2019/11/26 15:10
 **/
public class BasicException extends Exception {

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @Author big uncle
     * @Date 2019/11/26 15:13
     * @return
    **/
    private BasicException(){
    }

    public BasicException(Integer code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BasicException(IErrorCode iErrorCode){
        super(iErrorCode.getMsg());
        this.code = iErrorCode.getCode();
        this.msg = iErrorCode.getMsg();
    }

}
