package com.hogwarts.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * @author Jiangdanfeng
 * @since 2016-12-19
 */
public class DESedeUtils {

    /**
     * 加密/填充算法
     */
    private static final String ALGORITHM_3DES_CBC_PKCS5 = "DESede/CBC/PKCS5Padding";

    /**
     * 加密
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        byte[] ivs = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivp = new IvParameterSpec(ivs);

        Cipher cipher = Cipher.getInstance(ALGORITHM_3DES_CBC_PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, ivp);

        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] key, byte[] src) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);

        byte[] ivs = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivp = new IvParameterSpec(ivs);

        Cipher cipher = Cipher.getInstance(ALGORITHM_3DES_CBC_PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, securekey, ivp);
        return cipher.doFinal(src);
    }

    /**
     * 加密二进制原文
     *
     * @param keyHex 十六进制密钥
     * @param src
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String keyHex, byte[] src) throws Exception {
        return encrypt(resolveKey(keyHex), src);
    }

    /**
     * 加密二进制原文，返回Base64字符串
     *
     * @param keyHex
     * @param src
     * @return Base64
     * @throws Exception
     */
    public static String encryptBase64(String keyHex, byte[] src) throws Exception {
        return Base64.encodeBase64String(encrypt(resolveKey(keyHex), src));
    }

    /**
     * 解密二进制密文
     *
     * @param keyHex 十六进制密钥
     * @param src
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String keyHex, byte[] src) throws Exception {
        return decrypt(resolveKey(keyHex), src);
    }

    /**
     * 解密Base64后的密文
     *
     * @param keyHex
     * @param srcBase64
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String keyHex, String srcBase64) throws Exception {
        return decrypt(resolveKey(keyHex), Base64.decodeBase64(srcBase64));
    }

    /**
     * 解析十六进制密钥
     *
     * @param keyHex
     * @return
     * @throws Exception
     */
    private static byte[] resolveKey(String keyHex) throws Exception {
        final String k = StringUtils.reverse(keyHex);
        byte[] key = Hex.decodeHex(k.toCharArray());
        byte[] _key = new byte[24];
        if (key.length != 16) {
            throw new IllegalArgumentException("Illegal key length.");
        }
        System.arraycopy(key, 0, _key, 0, 16);
        System.arraycopy(key, 0, _key, 16, 8);
        return _key;
    }


    /**
     * main函数测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String key = "45C08B7A795A4A36803F63888122D640";
        System.out.println("key = " + key);

        String content = "万达网贷你好！";
        System.out.println("content = " + content);

        String encryptedContent = DESedeUtils.encryptBase64(key, content.getBytes("UTF-8"));
        System.out.println("encryptedContent = " + encryptedContent);

        byte[] decryptedContent = DESedeUtils.decryptBase64(key, encryptedContent);
        System.out.println("decryptedContent = " + new String(decryptedContent, "UTF-8"));
    }
}