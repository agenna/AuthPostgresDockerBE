package com.agenna.authPostgres.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

public class Jwt {
    @Getter
    private final String token;
    @Getter
    private final Long userId;
    @Getter
    private final LocalDateTime issueAt;
    @Getter
    private final LocalDateTime expiration;
    
    private Jwt(String token, Long userId, LocalDateTime issueAt, LocalDateTime expireAt) {
        this.token = token;
        this.userId = userId;
        this.issueAt = issueAt;
        this.expiration = expireAt;
    }

/*     public static Jwt of(String token){
        return new Jwt(token);
    } 
 */
    public static Jwt of(Long userId, Long validityInMinutes, String secretKey){
        Instant issueAt = Instant.now();
        Instant expiration = issueAt.plus(validityInMinutes,ChronoUnit.MINUTES);
        return new Jwt(
                        Jwts.builder()
                            .claim("user_id", userId)
                            .setIssuedAt(Date.from(issueAt))
                            .setExpiration(Date.from(expiration))
                            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString( secretKey.getBytes(StandardCharsets.UTF_8)))
                            .compact(),
                            userId,
                            LocalDateTime.ofInstant(issueAt,ZoneId.systemDefault()),
                            LocalDateTime.ofInstant(expiration,ZoneId.systemDefault()));
    }

    public static Jwt from(String token, String secretKey ){
        try
        {
            Claims claims =  ((Claims)(Jwts.parserBuilder()
            .setSigningKey(Base64.getEncoder().encodeToString(
                secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parse(token)
            .getBody()));
            Long userid = claims.get("user_id",Long.class);
            Date issueAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();
            return new Jwt(
                token, 
                userid, 
                LocalDateTime.ofInstant(Instant.ofEpochMilli(issueAt.getTime()), ZoneId.systemDefault()), 
                LocalDateTime.ofInstant(Instant.ofEpochMilli(expiration.getTime()), ZoneId.systemDefault()));
        }
        catch(Exception e){
            return null;
        }
    }
}
