package org.game.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.game.pojo.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTToken {

    public static Map userMap = new HashMap<String, String>();

    //加密的Key
    private static final String SECRET_KEY = "doudouyouxi";

    public static String buildJwt(User user) {

        //设置过期时间6小时
        long time = System.currentTimeMillis() + 60 * 60 * 1000;
        String jwt = Jwts
                .builder()
                //SECRET_KEY是加密算法对应的密钥，这里使用额是HS256加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                //expTime是过期时间
                .setExpiration(new Date(time))
                //该方法是在JWT中加入值为vaule的key字段
                .claim("key", JSON.toJSONString(user))
                .compact();
        userMap.put(user.getId(), jwt);
        System.out.println(userMap);
        return jwt;
    }

    public static User getUserFromJwt(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    //SECRET_KEY是加密算法对应的密钥，jjwt可以自动判断机密算法
                    .setSigningKey(SECRET_KEY)
                    //jwt是JWT字符串
                    .parseClaimsJws(jwt)
                    .getBody();
            String key = claims.get("key", String.class);
            User user = JSON.parseObject(key, User.class);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isJwtValid(String jwt, User user) throws Exception {
        try {
            //解析JWT字符串中的数据，并进行最基础的验证
            Claims claims = Jwts.parser()
                    //SECRET_KEY是加密算法对应的密钥，jwt可以自动判断机密算法
                    .setSigningKey(SECRET_KEY)
                    //jwt是JWT字符串
                    .parseClaimsJws(jwt)
                    .getBody();
            //获取自定义字段key
            String key = claims.get("key", String.class);
            //判断自定义字段是否正确
            if (JSON.toJSONString(user).equals(key)) {
                return true;
            } else {
                return false;
            }
            //在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
            //在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
        } catch (Exception e) {
                throw new Exception("用户登录超时！");
        }
    }

    /*public static boolean isJwtValid(String jwt, User user) {
        try {

            //解析JWT字符串中的数据，并进行最基础的验证
            Claims claims = Jwts.parser()
                    //SECRET_KEY是加密算法对应的密钥，jwt可以自动判断机密算法
                    .setSigningKey(SECRET_KEY)
                    //jwt是JWT字符串
                    .parseClaimsJws(jwt)
                    .getBody();
            //获取自定义字段key
            String key = claims.get("key", String.class);
            //判断自定义字段是否正确
            if (JSON.toJSONString(user).equals(key)) {
                return true;
            } else {
                return false;
            }
            //在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
            //在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
        } catch (Exception e) {
            try {
                throw new Exception("用户信息有误！");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }*/


}
