package com.hogwarts.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES加密解密
 */
@Slf4j
public class AESUtil {

    /**
     * AES密钥长度128(默认)/192/256
     */
    public static final int KEY_SIZE = 128;

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return String 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            // 创建密码器
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 设置为UTF-8编码
            final byte[] byteContent = content.getBytes("utf-8");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            final byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回
            return Base64.encodeBase64String(result);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return "";
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return String
     */
    public static String decrypt(String content, String password) {
        try {
            // 实例化
            final Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            // 执行操作
            final byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            // 采用UTF-8编码转化为字符串
            return new String(result, "utf-8");
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return "";
    }

    /**
     * 生成加密秘钥
     *
     * @param password 加密的密码
     * @return SecretKeySpec
     */
    private static SecretKeySpec getSecretKey(final String password) {
        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            // 生成一个密钥
            final SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (final NoSuchAlgorithmException ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 根据密钥，生成 aes.key
     *
     * @param password
     * @return
     */
    public static String getKeyByPass(String password) {
        SecretKeySpec keySpec = getSecretKey(password);
        byte[] b = keySpec.getEncoded();
        return byteToHexString(b);
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 随机生成AES对称密钥
     *
     * @return
     * @throws Exception
     */
    public static String generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        return Base64.encodeBase64String(keySpec.getEncoded());
    }

    /**
     * 加密
     *
     * @param data
     * @param secretKey
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, KEY_ALGORITHM);

        byte[] ivByte = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamsSpec);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    /**
     * 加密
     *
     * @param data
     * @param keyBase64
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String keyBase64) throws Exception {
        byte[] secretKey = Base64.decodeBase64(keyBase64);
        return encrypt(data, secretKey);
    }

    /**
     * 解密
     *
     * @param data
     * @param secretKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(secretKey, KEY_ALGORITHM);

        byte[] ivByte = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamsSpec);
        byte[] decryptedData = cipher.doFinal(data);
        return decryptedData;
    }

    /**
     * 解密
     *
     * @param data
     * @param keyBase64
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String keyBase64) throws Exception {
        byte[] secretKey = Base64.decodeBase64(keyBase64);
        return decrypt(data, secretKey);
    }

    /**
     * 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String secretKey = generateSecretKey();
        System.out.println("secretKey = " + secretKey);

        String content = "Hello, AES!";
        System.out.println(Base64.encodeBase64String(content.getBytes("UTF-8")));
        byte[] encryptData = encrypt(content.getBytes("UTF-8"), secretKey);
        String encryptContent = Base64.encodeBase64String(encryptData);
        System.out.println("encryptContent = " + encryptContent);

        byte[] decryptData = decrypt(encryptData, secretKey);
        String decryptContent = new String(decryptData, "UTF-8");
        System.out.println("decryptContent = " + decryptContent);
    }
}