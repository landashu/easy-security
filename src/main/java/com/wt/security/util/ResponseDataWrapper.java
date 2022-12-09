package com.wt.security.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthProperties;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResponseDataWrapper extends HttpServletResponseWrapper {

    private static final Logger log = LoggerFactory.getLogger(ResponseDataWrapper.class);

    private ByteArrayOutputStream buffer = null;
    private ServletOutputStream out = null;
    private PrintWriter writer = null;

    public ResponseDataWrapper(HttpServletResponse response) throws IOException {
        super(response);
        //真正存储数据的流
        buffer = new ByteArrayOutputStream();
        out = new WapperedOutputStream(buffer);
        writer = new PrintWriter(new OutputStreamWriter(buffer,Charsets.UTF_8.toString()));
    }

    @Override
    public ServletOutputStream getOutputStream()throws IOException {
        return out;
    }

    @Override
    public PrintWriter getWriter() throws UnsupportedEncodingException {
        return writer;
    }

    @Override
    public void flushBuffer()throws IOException{
        if(out!=null){
            out.flush();
        }
        if(writer!=null){
            writer.flush();
        }
    }

    @Override
    public void reset(){
        buffer.reset();
    }

    public void changeContent(HttpServletResponse response, AuthProperties authProperties, ObjectMapper mapper) throws IOException{

        flushBuffer();
        PrintWriter out = null;
        String data = null;
        try {
            data = buffer.toString(Charsets.UTF_8.toString());
            out = response.getWriter();
            String iv = AesEncryptUtil.getRandomString(16);
            response.setHeader("iv",iv);
            response.setCharacterEncoding(Charsets.UTF_8.toString());
            if(StrUtil.isEmpty(data)){
                return;
            }
            ResponseData<Object> responseData = mapper.readValue(data.trim(),ResponseData.class);
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            if(!ObjectUtil.isEmpty(responseData.getData()) && threadLocalEntity.getDecrypt()){
                String obj = AesEncryptUtil.encrypt(mapper.writeValueAsString(responseData.getData()), authProperties.getKey(),iv).trim();
                if(StrUtil.isEmpty(obj)){
                    throw new BasicException(BasicCode.BASIC_CODE_99988);
                }
                responseData.setData(obj);
            }
            data = mapper.writeValueAsString(responseData);
        }catch (Exception e){
            e.getMessage();
            log.error(e.getMessage());
        }finally {
            if(out != null){
                out.write(data);
                out.flush();
                out.close();
            }
        }
    }

    private Object encrypt(ResponseData<Object> responseData,String key,String iv){
        Object obj = responseData.getData();
        if(obj.getClass().equals(String.class)){
            obj = toString(obj,key,iv);
        }
        if(obj.getClass().equals(LinkedHashMap.class)){
            obj = toLinkedHashMap(obj,key,iv);
        }
        if(obj.getClass().equals(ArrayList.class)){
            obj = toArray(obj,key,iv);
        }
        return obj;
    }

    private Object toString(Object obj,String key,String iv){
        if(ObjectUtil.isEmpty(obj)){
            return obj;
        }
        return AesEncryptUtil.encrypt(obj.toString(),key,iv);
    }

    private Object toArray(Object obj,String key,String iv){
        return obj;
    }

    private Object toLinkedHashMap(Object obj,String key,String iv){
        LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) obj;
        linkedHashMap.keySet().stream().forEach(i -> {
            Object ov = linkedHashMap.get(i);
            if(!ObjectUtil.isEmpty(ov)) {
                String value = String.valueOf(ov).trim();
                linkedHashMap.put(i, AesEncryptUtil.encrypt(value,key,iv).trim());
            }
        });
        return linkedHashMap;
    }


    private class WapperedOutputStream extends ServletOutputStream{

        private ByteArrayOutputStream bos=null;

        public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException{
            bos=stream;
        }
        @Override
        public void write(int b) throws IOException{
            bos.write(b);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }


}
