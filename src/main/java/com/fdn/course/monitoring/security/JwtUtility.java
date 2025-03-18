package com.fdn.course.monitoring.security;

import com.fdn.course.monitoring.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtility {

    public Map<String, Object> mappingBodyToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", claims.get("id"));
        map.put("username", claims.getSubject());
        map.put("noHp", claims.get("phn"));
        map.put("namaLengkap", claims.get("nl"));
        map.put("email", claims.get("ml"));
        return map;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String doGenerateToken(Map<String, Object> claims, String subject){
        long timeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(timeMillis))
                .setExpiration(new Date(timeMillis + JwtConfig.getTimeExpiration()))
                .signWith(SignatureAlgorithm.HS512, JwtConfig.getSecretKey())
                .compact();
    }

    public Boolean validateToken(String token){
        String username = getUsernameFromToken(token);
        return (username != null && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}
