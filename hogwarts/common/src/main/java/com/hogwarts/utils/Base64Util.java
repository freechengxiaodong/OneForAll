package com.hogwarts.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Description:Base64编码、解密工具类
 * Date: 2015/11/3 9:19
 * Author: zhaozhiwei
 */
public class Base64Util {
    /**
     * base64编码，并返回字符串
     *
     * @param source 待做编码的数据
     * @return
     */
    public static String encode(byte[] source) {
        return Base64.encodeBase64String(source);
    }

    /**
     * base64解码
     *
     * @param base64String 待解码的字符串
     * @return
     */
    public static byte[] decode(String base64String) {
        return Base64.decodeBase64(base64String);
    }
}
