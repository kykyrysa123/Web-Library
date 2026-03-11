package com.example.weblibrary.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  @Value("${jwt.refresh-expiration}")
  private Long refreshExpiration;

  private Key getSigingKey(){
    byte[] keyBytes = secret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);

  }

  public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token){
    return Jwts.parserBuilder()
            .setSigningKey(getSigingKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(UserDetails userDetails){
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims,userDetails.getUsername(),expiration);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(), refreshExpiration);
  }

  private String createToken(Map<String, Object> claims, String subject, Long expiration){
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+expiration))
            .signWith(getSigingKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails){
    try{
     final String username = extractUsername(token);
     return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    catch(MalformedJwtException e){
      log.error("Invalid JWT token {}", e.getMessage());
    }
    catch(ExpiredJwtException e){
      log.error("JWT token is expired: {}", e.getMessage());
    }
    catch(UnsupportedJwtException e){
      log.error("JWT token is unsupported: {}", e.getMessage());
    }
    catch(IllegalArgumentException e){
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
