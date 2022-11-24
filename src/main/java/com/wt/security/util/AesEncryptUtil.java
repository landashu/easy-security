package com.wt.security.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * @author big uncle
 * @date 2021/3/16 14:17
 * @module
 **/
public class AesEncryptUtil {

    private static String INSTANCE = "AES/CBC/NoPadding";

    private static String ALGORITHM = "AES";

    public static final String IV = "iv";

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv){
        try {
            //"算法/模式/补码方式"NoPadding PkcsPadding
            Cipher cipher = Cipher.getInstance(INSTANCE);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) {
        try {
            byte[] encrypted1 = new Base64().decode(data);
            Cipher cipher = Cipher.getInstance(INSTANCE);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), ALGORITHM);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, Charsets.UTF_8.toString());
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRandomString(int length){
        return UUID.randomUUID().toString().replace("-","").substring(length);
    }


    /**
     * 测试
     */
//    public static void main(String args[]) throws Exception {
//
//        System.out.println();
//
//        String test = "yppu";
//        String key =  "platformSecurity";
//        String iv = "b65ac06676384164";
//
//        String data = encrypt(test, key, iv);
//        System.out.println("加密前："+test);
//        System.out.println("加密后："+data);
//        String jiemi = desEncrypt("lmS0ZR7hjCNZxoqaNRKieQ==", key, iv).trim();
//        System.out.println("解密："+jiemi);
//    }


}
