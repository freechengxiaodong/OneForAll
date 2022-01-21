package com.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * @author
 * @date 2019/10/28
 */
@Slf4j
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * 加解密key
     */
    public static final String AES_SECRET_KEY = "QnhjIeoN12P.1234";


    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key 加密密钥
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes("utf-8");
            //初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回
            return Base64Utils.encodeToString(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content 加密信息
     * @param key 密钥
     * @return 解密内容
     */
    public static String decrypt(String content, String key) {
        try {
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return 密钥
     */
    private static SecretKeySpec getSecretKey(final String key) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        //KeyGenerator kg = null;
        //try {
        //    kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //    //AES 要求密钥长度为 128
        //    kg.init(128, new SecureRandom(key.getBytes()));
        //    //生成一个密钥
        //    SecretKey secretKey = kg.generateKey();
        //    // 实例化
        //    return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        //} catch (NoSuchAlgorithmException ex) {
        //    ex.printStackTrace();
        //}
        try {
            byte[] raw = key.getBytes("utf-8");
            return new SecretKeySpec(raw, KEY_ALGORITHM);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("1234qwer", AES_SECRET_KEY));
    }
}
