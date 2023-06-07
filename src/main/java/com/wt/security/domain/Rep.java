package com.wt.security.domain;

import cn.hutool.http.HttpStatus;
import com.wt.security.exp.IErrorCode;


public class Rep<T>{

    private Integer code;
    private String msg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    protected Rep(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Rep ok(){
        return new Rep(HttpStatus.HTTP_OK,null,null);
    }

    public static Rep ok(Object data){
        return new Rep(HttpStatus.HTTP_OK,null,data);
    }

    public static Rep ok(IErrorCode iErrorCode){
        return new Rep(iErrorCode.getCode(),iErrorCode.getMsg(),null);
    }

    public static Rep ok(IErrorCode iErrorCode, Object obj){
        return new Rep(iErrorCode.getCode(),iErrorCode.getMsg(),obj);
    }

    public static Rep error(IErrorCode iErrorCode){
        return new Rep(iErrorCode.getCode(),iErrorCode.getMsg(),null);
    }

    public static Rep error(Rep rep){
        return new Rep(rep.getCode(), rep.getMsg(),null);
    }

    public static Rep error(IErrorCode iErrorCode, Object obj){
        return new Rep(iErrorCode.getCode(),iErrorCode.getMsg(),obj);
    }

    public static Rep error(Integer code, String msg){
        return new Rep(code,msg,null);
    }

    public Rep() {
    }

}