package com.wt.security.server.wrapper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wt.security.code.BasicCode;
import com.wt.security.exp.impl.BasicException;
import com.wt.security.properties.SecurityProperties;
import com.wt.security.server.encryption.CiphertextServer;
import com.wt.security.server.encryption.impl.AesEncryptServer;
import com.wt.security.domain.Req;
import com.wt.security.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;


public class RequestDataWrapper extends HttpServletRequestWrapper {

    private static final Logger log = LoggerFactory.getLogger(RequestDataWrapper.class);
    private String body;
    private CiphertextServer ciphertextServer = new AesEncryptServer();
    private ObjectMapper mapper = new ObjectMapper();
    public RequestDataWrapper(HttpServletRequest request, SecurityProperties securityProperties) throws Exception {
        super(request);
        body = getBodyContent(request);
        Req<Object,Object> req = new Req<Object,Object>();
        if(!StrUtil.isEmpty(body)) {
            req = mapper.readValue(body, Req.class);
            if (ObjectUtil.isEmpty(req)) {
                req = new Req<Object,Object>();
            }
            ThreadLocalUtil.ThreadLocalEntity threadLocalEntity = ThreadLocalUtil.threadLocal.get();
            req.setUser(threadLocalEntity.getUser());
            if(threadLocalEntity.getDecrypt()) {
                decrypt(req,request, securityProperties.getSecretKey());
            }
        }
        String token = request.getHeader(securityProperties.getTokenKey());
        req.setToken(token);
        body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
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

    private void decrypt(Req data, HttpServletRequest request, String key) throws BasicException, JsonProcessingException {
        Object obj = data.getData();
        String iv = request.getHeader(CiphertextServer.IV);
        if(StrUtil.isEmpty(iv)){
            throw new BasicException(BasicCode.BASIC_CODE_99989);
        }
        if(ObjectUtil.isEmpty(obj)){
            return;
        }
        String json = ciphertextServer.decryption(mapper.writeValueAsString(obj),key,iv).trim();
        if(StrUtil.isEmpty(json)){
            throw new BasicException(BasicCode.BASIC_CODE_99987);
        }
        data.setData(mapper.readValue(json,LinkedHashMap.class));
    }


}
