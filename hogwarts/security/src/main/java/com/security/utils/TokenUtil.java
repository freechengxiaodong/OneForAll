package com.security.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author
 * @date 2019/10/9
 */
public class TokenUtil {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "synjonessso";
    private static final String ISS = "synjones";

    // 过期时间是7天
    public static final long EXPIRATION = 604800L;
    /**
     * 是否使用JWT设置token
     */
    public static final boolean TOKEN_JWT = false;


    ///**
    // * 创建token
    // * 注：如果是根据可变的唯一值来生成，唯一值变化时，需重新生成token
    // * @param username 用户名
    // * @return
    // */
    //private static String createJwtToken(String id, String username) {
    //   return JWT.create()
    //           .withIssuer(ISS)
    //           .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
    //           .withIssuedAt(new Date())
    //           .withSubject(id)
    //           .withAudience(username)
    //           .sign(Algorithm.HMAC256(SECRET+username));
    //}
    //
    //private static String getUsername(String token) {
    //   return JWT.decode(token).getAudience().get(0);
    //}
    //
    ///**
    // * JWT签名验证
    // * @param token token
    // * @return
    // */
    //private static boolean verifyJwt(String token){
    //    try {
    //        String name = getUsername(token);
    //        if (StringUtils.isBlank(name)) {
    //            return false;
    //        }
    //        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET+name))
    //                .withIssuer(ISS)
    //                .build();
    //        DecodedJWT jwt = verifier.verify(token);
    //        System.out.println("认证通过：");
    //        System.out.println("issuer: " + jwt.getIssuer());
    //        System.out.println("username: " + jwt.getAudience().get(0));
    //        System.out.println("过期时间：      " + jwt.getExpiresAt());
    //        return true;
    //    } catch (Exception e){
    //        return false;
    //    }
    //}

    /**
     * 两种方式设置token
     * @param id id
     * @param username 用户名
     * @return token
     */
    public static String createToken(String id, String username) {
        //if (TOKEN_JWT) {
        //    return createJwtToken(id, username);
        //} else {
            return IdGeneratorUtil.uuid();
        //}
    }

    /**
     * 验证token
     * @param token token
     * @return token是否正确
     */
    public static boolean verify(String token) {
        //if (TOKEN_JWT) {
        //    return verifyJwt(token);
        //} else {
            if (StringUtils.isNotBlank(token)) {
                return true;
            }
            return false;
        //}
    }

    public static String getHeaderToken(String header) {
        if (StringUtils.isBlank(header)) {
            return null;
        }
        if (!header.startsWith(TOKEN_PREFIX) || header.length() <= TOKEN_PREFIX.length()) {
            return null;
        }
        return header.substring(TOKEN_PREFIX.length(), header.length());
    }

}
