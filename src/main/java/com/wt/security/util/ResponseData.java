package com.wt.security.util;

import cn.hutool.http.HttpStatus;
import com.wt.security.exp.IErrorCode;


public class ResponseData <T>{

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

    protected ResponseData(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseData successResponse(){
        return new ResponseData(HttpStatus.HTTP_OK,null,null);
    }

    public static ResponseData successResponse(Object data){
        return new ResponseData(HttpStatus.HTTP_OK,null,data);
    }

    public static ResponseData successResponse(IErrorCode iErrorCode){
        return new ResponseData(iErrorCode.getCode(),iErrorCode.getMsg(),null);
    }

    public static ResponseData successResponse(IErrorCode iErrorCode,Object obj){
        return new ResponseData(iErrorCode.getCode(),iErrorCode.getMsg(),obj);
    }

    public static ResponseData failureResponse(IErrorCode iErrorCode){
        return new ResponseData(iErrorCode.getCode(),iErrorCode.getMsg(),null);
    }

    public static ResponseData failureResponse(ResponseData responseData){
        return new ResponseData(responseData.getCode(),responseData.getMsg(),null);
    }

    public static ResponseData failureResponse(IErrorCode iErrorCode,Object obj){
        return new ResponseData(iErrorCode.getCode(),iErrorCode.getMsg(),obj);
    }

    public static ResponseData failureResponse(Integer code,String msg){
        return new ResponseData(code,msg,null);
    }

    public ResponseData() {
    }

}
