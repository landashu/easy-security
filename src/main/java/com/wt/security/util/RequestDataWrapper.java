package com.wt.security.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.AuthenticationProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;

/**
 * @Author big uncle
 * @Date 2019/11/28 17:30
 **/

public class RequestDataWrapper extends HttpServletRequestWrapper {

    private static final Log log = LogFactory.getLog(RequestDataWrapper.class);
    private String body;

    public RequestDataWrapper(HttpServletRequest request, AuthenticationProperties authenticationProperties,
                              ObjectMapper mapper) throws Exception {
        super(request);
        body = getBodyContent(request);
        RequestData<Object,Object> requestData = new RequestData<Object,Object>();
        if(!StrUtil.isEmpty(body)) {
            requestData = mapper.readValue(body, RequestData.class);
            if (ObjectUtil.isEmpty(requestData)) {
                requestData = new RequestData<Object,Object>();
            }
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            requestData.setUser(threadLocalEntity.getUser());
            if(threadLocalEntity.getDecrypt()) {
                decrypt(requestData,request,authenticationProperties.getKey(),mapper);
            }
        }
        String token = request.getHeader(authenticationProperties.getToken());
        requestData.setToken(token);
        body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestData);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes(Charset.forName("UTF-8")));

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
            @Override
            public boolean isFinished() {
                return false;
            }
            @Override
            public boolean isReady() {
                return false;
            }
            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    private String getBodyContent(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 解密
     * @Author big uncle
     * @Date 2021/7/6 10:40
     * @param data
     * @return void
    **/
    private void decrypt(RequestData data,HttpServletRequest request,
                         String key,ObjectMapper mapper) throws BasicException, JsonProcessingException {
        Object obj = data.getData();
        String iv = request.getHeader(AesEncryptUtil.IV);
        if(StrUtil.isEmpty(iv)){
            throw new BasicException(BasicCode.BASIC_CODE_99989);
        }
        if(ObjectUtil.isEmpty(obj)){
            return;
        }
        String json = AesEncryptUtil.desEncrypt(mapper.writeValueAsString(obj),key,iv).trim();
        if(StrUtil.isEmpty(json)){
            throw new BasicException(BasicCode.BASIC_CODE_99987);
        }
        data.setData(mapper.readValue(json,LinkedHashMap.class));
//        if(obj.getClass().equals(String.class)){
//            toString(obj,data,key,iv);
//        }
//        if(obj.getClass().equals(LinkedHashMap.class)){
//            toLinkedHashMap(obj,data,key,iv);
//        }
//        if(obj.getClass().equals(ArrayList.class)){
//            toArray(obj,data,key,iv);
//        }
    }

    private void toString(Object obj,RequestData data,String key,String iv){
        // TODO 转换 为 String 数据解密
        data.setData(obj);
    }

    private void toArray(Object obj,RequestData data,String key,String iv){
        // TODO 转换 为 Array 数据解密
        data.setData(obj);
    }

    private void toLinkedHashMap(Object obj,RequestData data,String key,String iv){
        LinkedHashMap<String,Object> linkedHashMap = (LinkedHashMap) obj;
        linkedHashMap.keySet().stream().forEach(i -> {
            Object ov = linkedHashMap.get(i);
            if(!ObjectUtil.isEmpty(ov)) {
                String value = String.valueOf(ov).trim();
                linkedHashMap.put(i, AesEncryptUtil.desEncrypt(value,key,iv).trim());
            }
        });
        data.setData(linkedHashMap);
    }


}
