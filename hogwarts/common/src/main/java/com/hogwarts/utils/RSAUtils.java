package com.hogwarts.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA Common Utils
 *
 * @author Jiangdanfeng
 * @since 2017-04-21
 */
public abstract class RSAUtils {

    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名验证算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 密钥长度，默认1024，密钥长度必须是64的整数倍，范围在512-65536之间
     */
    public static final int KEY_SIZE = 1024;

    public static final String PUBLIC_KEY = "RSAPublicKey";

    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 生成RSA密钥对
     *
     * @throws Exception
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyBase64 = Base64.encodeBase64String(publicKey.getEncoded());
        String privateKeyBase64 = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> map = new HashMap<>(2);
        map.put(PUBLIC_KEY, publicKeyBase64);
        map.put(PRIVATE_KEY, privateKeyBase64);
        return map;
    }

    private static RSAPublicKey buildRSAPublicKey(String publicKeyBase64) throws Exception {
        byte[] pubKey = Base64.decodeBase64(publicKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static RSAPrivateKey buildRSAPrivateKey(String privateKeyBase64) throws Exception {
        byte[] privKey = Base64.decodeBase64(privateKeyBase64);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKey);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 私钥签名
     *
     * @param data       待签名数据
     * @param privateKey RSA私钥
     * @return byte[] 数字签名
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        KeyFactory keyf = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey signKey = keyf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(signKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 私钥签名
     *
     * @param data
     * @param privateKey Base64
     * @return String Base64
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] key = Base64.decodeBase64(privateKey);
        byte[] signValue = sign(data, key);
        return Base64.encodeBase64String(signValue);
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param sign
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKey));
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 公钥验签
     *
     * @param data      待验签数据
     * @param sign      签名Base64
     * @param publicKey Base64
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String sign, String publicKey) throws Exception {
        byte[] key = Base64.decodeBase64(publicKey);
        byte[] signValue = Base64.decodeBase64(sign);
        return verify(data, signValue, key);
    }

    /**
     * 使用RSA公钥包装对称密钥
     *
     * @param keySpec
     * @param publicKeyBase64
     * @return
     * @throws Exception
     */
    public static String wrapSecretKey(SecretKeySpec keySpec, String publicKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.WRAP_MODE, buildRSAPublicKey(publicKeyBase64));
        byte[] wrappedKey = cipher.wrap(keySpec);
        return Base64.encodeBase64String(wrappedKey);
    }

    public static String wrapSecretKey(String secretKeyBase64, String publicKeyBase64) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(secretKeyBase64), KEY_ALGORITHM);
        return wrapSecretKey(keySpec, publicKeyBase64);
    }

    /**
     * 使用RSA私钥解密上送的对称密钥
     *
     * @param wrappedKeyBase64
     * @param privateKeyBase64
     * @return
     * @throws Exception
     */
    public static SecretKeySpec unwrapSecretKey(String wrappedKeyBase64, String privateKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.UNWRAP_MODE, buildRSAPrivateKey(privateKeyBase64));

        byte[] wrappedKey = Base64.decodeBase64(wrappedKeyBase64);
        Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        return keySpec;
    }

    /**
     * RSA公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥加密
     *
     * @param data
     * @param keyBase64 Base64
     * @return byte[] 密文
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String keyBase64) throws Exception {
        return encryptByPublicKey(data, Base64.decodeBase64(keyBase64));
    }

    /**
     * RSA私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA私钥加密
     *
     * @param data
     * @param keyBase64
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String keyBase64) throws Exception {
        return encryptByPrivateKey(data, Base64.decodeBase64(keyBase64));
    }

    /**
     * RSA公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥解密
     *
     * @param data
     * @param keyBase64
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String keyBase64) throws Exception {
        return decryptByPublicKey(data, Base64.decodeBase64(keyBase64));
    }

    /**
     * RSA私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA私钥解密
     *
     * @param data
     * @param keyBase64
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String keyBase64) throws Exception {
        return decryptByPrivateKey(data, Base64.decodeBase64(keyBase64));
    }

    /**
     * 测试
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
		/*Map<String, String> keyMap = RSAUtils.generateKeyPair();
		System.out.println(keyMap);*/

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIXrvrxbb6DNIm2swBkWAWu+4k4EyDHKMmzX1WbRXnwcRH45ZZgCewSPl1EPHlvTjIrq14wGS8G7GPGIUDRiWsOMibaZUYeiqCqqzljZ6O4CUpbdFGxKhf7anhdN29RcOC7xwcwE7sBiXQKDEdX+GicY7AyR9+mabN6VEybcYfRXAgMBAAECgYAzVuDUDKmKl5lvOh6af1kkGRVgm2yzybPzDfr9Y545Lg0D5rUPxUOrx/BWXtlgkoT6bHUddPxt1HAHiB5XNq1cY4r1/GYvszV62pcJQkV2eXAMs+aaCFW7YZ5EGAJFGhxoOmmKCnEe/8Wh+i/Phrf5jYWoqFlg09oRRh3v+4MbAQJBAMgh7Ma6ysGDZ2b02pUNCOwaVFyOF54gMgzHmpuhjWy0NEsBcoVKb5vQl0Tl7MbdVLqtpQChq4IOH0JsuyKfCHcCQQCrTiLYIVA+3+bYjkF8V+nTbsWe2wvud/CtdZaYUoRFs/fPc1ALAF9wdiW2OdUtitRZmvvyUd6WSEc/lVzVkUshAkAdzF9S37EcyxH0VHTPSJeRgAIbndSKaMTK/lVY4t8J1nMKz/ZbPlAL5S0AxzNQENhyyzQMOgYtYSjqXQIoD4HVAkAkFkvE2A+9jxFkYWNuPF7nyZgcBO0YiW/MOs9Eu21d+bGkpBgimSuB14HOR5SfCXX+gmW9AH4xjnxj6qUp4HfBAkAuDgEvwSHerAfxuQwtTwnCSGlEFEU3MrNJENJlZEBzGu6ev7k+T0zv6NoYSHZznRzTgp53JT34/H7fHMoDzkVh";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF6768W2+gzSJtrMAZFgFrvuJOBMgxyjJs19Vm0V58HER+OWWYAnsEj5dRDx5b04yK6teMBkvBuxjxiFA0YlrDjIm2mVGHoqgqqs5Y2ejuAlKW3RRsSoX+2p4XTdvUXDgu8cHMBO7AYl0CgxHV/honGOwMkffpmmzelRMm3GH0VwIDAQAB";

        String secretKey = "fxyl6BG9JbsmS/SowFY+jw==";

        String content = "Hello world, RSA!!!Hello world, RSA!!!Hello world, RSA!!!Hello world, RSA!!!";

        //RSA数字签名
        String sign = RSAUtils.sign(content.getBytes("UTF-8"), privateKey);
        System.out.println("sign = " + sign);
        //RSA验签
        boolean verifyFlag = RSAUtils.verify(content.getBytes("UTF-8"), sign, publicKey);
        System.out.println("verifyFlag = " + verifyFlag);

        //RSA私钥加密
        byte[] privateEncryptContent = RSAUtils.encryptByPrivateKey(content.getBytes("UTF-8"), privateKey);
        System.out.println("privateEncryptContent = " + Base64.encodeBase64String(privateEncryptContent));

        //RSA公钥解密
        byte[] publicDecryptContent = RSAUtils.decryptByPublicKey(privateEncryptContent, publicKey);
        System.out.println("publicDecryptContent = " + new String(publicDecryptContent, "UTF-8"));

        //RSA公钥加密
        byte[] publicEncryptContent = RSAUtils.encryptByPublicKey(content.getBytes("UTF-8"), publicKey);
        System.out.println("publicEncryptContent = " + Base64.encodeBase64String(publicEncryptContent));

        //RSA私钥解密
        byte[] privateDecryptContent = RSAUtils.decryptByPrivateKey(publicEncryptContent, privateKey);
        System.out.println("privateDecryptContent = " + new String(privateDecryptContent, "UTF-8"));

        //RSA包装AES对称密钥
        String wrapKey = RSAUtils.wrapSecretKey(secretKey, publicKey);
        System.out.println("wrapKey = " + wrapKey);

        //RSA解包AES对称密钥
        SecretKeySpec secretKeySpec = RSAUtils.unwrapSecretKey(wrapKey, privateKey);
        String unwrapKey = Base64.encodeBase64String(secretKeySpec.getEncoded());
        System.out.println("unwrapKey = " + unwrapKey);

        //RSA加密AES对称密钥
        byte[] wrapKey2 = RSAUtils.encryptByPrivateKey(Base64.decodeBase64(secretKey), privateKey);
        System.out.println("wrapKey2 = " + Base64.encodeBase64String(wrapKey2));

        //RSA解密AES对称密钥
        byte[] unwrapKey2 = RSAUtils.decryptByPublicKey(wrapKey2, publicKey);
        System.out.println("unwrapKey2 = " + Base64.encodeBase64String(unwrapKey2));

        System.out.println("wrapKey == wrapKey2 ? " + wrapKey.equals(Base64.encodeBase64String(wrapKey2)));
        System.out.println("unwrapKey == unwrapKey2 ? " + unwrapKey.equals(Base64.encodeBase64String(unwrapKey2)));

        //AES加密
        byte[] encryptData = AESUtil.encrypt(content.getBytes("UTF-8"), secretKey);
        System.out.println("encryptData = " + Base64.encodeBase64String(encryptData));

        //AES解密
        byte[] decryptData = AESUtil.decrypt(encryptData, unwrapKey);
        System.out.println("decryptData = " + new String(decryptData, "UTF-8"));

    }

}
