package com.security.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author
 * @date 2019/10/9
 */
public class MD5Util {

    private MD5Util() {
    }

    public static String encrypt(String plainText) {
        return StringUtils.isBlank(plainText)?null:md5(plainText);
    }

    private static String md5(String plainText) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(plainText.getBytes());
            byte[] bytes = md5.digest();
            result = bytesToHex(bytes);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        }
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        int len = bytes.length;

        for(int i = 0; i < len; ++i) {
            byte aByte = bytes[i];
            int digital = aByte;
            if(aByte < 0) {
                digital = aByte + 256;
            }
            if(digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }

        return md5str.toString().toUpperCase();
    }
}
