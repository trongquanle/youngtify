package com.youngtify.config;

import com.youngtify.model.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 30;

    @Value("${jwt.secret}")
    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        try {
            return String.valueOf(claims.get("username"));
        } catch (Exception e) {
            return null;
        }
//        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserProfile userProfile) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userProfile.getId());
        claims.put("username", userProfile.getUsername());
        claims.put("firstName", userProfile.getFirstName());
        claims.put("lastName", userProfile.getLastName());
        claims.put("email", userProfile.getEmail());
        claims.put("avatar", userProfile.getAvatarUrl());
        if (userProfile.getDateOfBirth() != null)
            claims.put("dateOfBirth", userProfile.getDateOfBirth().toString());
        if (userProfile.getGender() != null)
            claims.put("gender", userProfile.getGender());
        if (userProfile.getPhoneNumber() != null)
            claims.put("phoneNumber", userProfile.getPhoneNumber());
        return doGenerateToken(claims);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims) {
        long mil = System.currentTimeMillis();
        Date dateIat = new Date(mil);
        Date dateExp = new Date(mil + JWT_TOKEN_VALIDITY * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(dateIat)
                .setExpiration(dateExp)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
