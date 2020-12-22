package com.kakuom.finaltipping.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    public static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("role", userPrincipal.getAuthorities())
                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            var t = Jwts.parser().setSigningKey(JwtProperties.SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (ExpiredJwtException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        }

        return false;
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtProperties.SECRET)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
