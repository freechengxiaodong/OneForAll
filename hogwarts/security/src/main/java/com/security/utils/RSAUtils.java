package com.security.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date 2019/11/11
 */
public class RSAUtils {
    /**
     * 用于封装随机产生的公钥与私钥
     */
    private static Map<String, String> keyMap = new HashMap<String, String>();
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAinKI4nyGoLZZwJ+/TRGkxJ9JyqRb8OInSa4s2/Cnei+ySl3bGstd3uaelRQ836cv74c7/" +
            "xJpLHcxLAqQI+fCGMpUkrdK6IaCUeYDvhY+b+nPPZX6UrwFa46DInaiXnnmvCCkaXPWLT+kJN1r5XBG451NRaH+Wuo3MzmTahASSQIDAQAB";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICKcojifIagtlnAn79NEaTEn0nKpFvw4idJrizb8Kd6L7JKXdsay13e5p6VFDzfpy" +
            "/vhzv/EmksdzEsCpAj58IYylSSt0rohoJR5gO+Fj5v6c89lfpSvAVrjoMidqJeeea8IKRpc9YtP6Qk3WvlcEbjnU1Fof5a6jczOZNqEBJJAgMBAAEC" +
            "gYAh31nrRHO4LmWfJSlzieWaW8317hZ9u+58l+f0Hkk+eO5Ut1AgSuFIbVKse3CH0hAGi8JBRygc2wDVzltCPJP3rCUpYBVrzN+yF5golG1agUe/B4" +
            "EvM+/j0TAquU71I8mfxhWftmgO+1cIaWgNVefA0q3APb+GRJQUAaDRJuwbAQJBAMKd1qXNatB3IQRwFr3+p5VBuSYmTGHkOeS1li4XCB6gZ4i8LEod" +
            "I9h+olxBa6KyAPPcQLjHwePIXQiKHPJQUrECQQCpFV/f8jU0ng5Fv681MMZ1HNSsFQJUHSR7E+yMHESeUpSy8R3Y9eiuu1GG57zQndvD1rUKUjdsg3" +
            "hs9qIVAa8ZAkBfSZjHJYSe/TUVTyxTalPzKHLW0vmjFHOkH6SgfHZlSHRo5nlo8EuUvKwSL5Xb2vvMapLl5ihwtrGWdMkne1yBAkEApmpyNaFXOpWB" +
            "6KZUFN8PxT5+F2yKIS2ZtUWT4QcFhmozDrgAL4VA4ZFeGZBGJ3HnViYhg6oOANCZG23XcyN4KQJAXbC1aqVX7RpWNensUT3y5P/omTUGU0FLrml6kc" +
            "K9fN/IgXpj6F4OCdJehK0sM280jIomFNsatBpSBXdLaMinjQ==";
    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        //0表示公钥
        keyMap.put("PublicKey",publicKeyString);
        //1表示私钥
        keyMap.put("PrivateKey",privateKeyString);
    }

    /**
     * 根据公钥进行加密
     * @param str 内容
     * @return 结果
     */
    public static String encryptByPublicKey(String str) {
        try {
            return encryptByPublicKey(str, PUBLIC_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encryptByPublicKey(String str, String publicKey) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * 使用私钥进行解密
     * @param str 需解密内容
     * @return 结果
     */
    public static String decryptByPrivateKey(String str) {
        try {
            return decryptByPrivateKey(str, PRIVATE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decryptByPrivateKey(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 根据私钥进行加密
     * @param str 内容
     * @return 加密结果
     */
    public static String encryptByPrivateKey( String str) {
        try {
            return encryptByPrivateKey(str, PRIVATE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA私钥加密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encryptByPrivateKey( String str, String privateKey) throws Exception{
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * 根据默认公钥解密
     * @param str 解密内容
     * @return 解密结果
     */
    public static String decryptByPublicKey(String str) {
        try {
            return decryptByPublicKey(str, PUBLIC_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA公钥解密
     *
     * @param str
     *            加密字符串
     * @param pubilcKey
     *            公钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decryptByPublicKey(String str, String pubilcKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(pubilcKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        try {
            return Base64.encodeBase64String(PUBLIC_KEY.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        //genKeyPair();
        //加密字符串
        //String message = "df723820";
        //String messageEn = encryptByPublicKey(message);
        //System.out.println(message + "\t加密后的字符串为:" + messageEn);
        //String messageDe = decryptByPrivateKey(messageEn);
        //System.out.println("还原后的字符串为:" + messageDe);
        //
        //
        //String messageEnP = encryptByPrivateKey(message);
        //System.out.println(message + "\t加密后的字符串为:" + messageEnP);
        //String messageDep = decryptByPublicKey(messageEnP);
        //System.out.println("还原后的字符串为:" + messageDep);
        //System.out.println(PUBLIC_KEY);
        //System.out.println(PUBLIC_KEY.length());
        //System.out.println(getPublicKey());
        //System.out.println(getPublicKey().length());
        System.out.println(encryptByPublicKey("888888"));

    }
}
