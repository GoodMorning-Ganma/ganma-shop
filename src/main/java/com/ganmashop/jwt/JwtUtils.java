package com.ganmashop.jwt;

import com.ganmashop.utils.GenUUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author Desmondlzk
 * Date: 06/02/2024 - 8:08 PM
 */
public class JwtUtils {

    private JwtUtils(){};

    //有效期为
    public static final Long JWT_TTL = 60 * 60 * 1000L; // 一个小时
    //设置JWT密钥
    public static final String JWT_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    /**
     * 生成JWT
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, GenUUID.getUUID());
        return builder.compact();
    }

    /**
     * 生成JWT
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, GenUUID.getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis == null){
            ttlMillis = JwtUtils.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)           // 唯一的ID
                .setSubject(subject)   // 主题可以是JSON数据
                .setIssuer("desmond")  // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成JWT
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成加密后的秘钥
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtils.JWT_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
