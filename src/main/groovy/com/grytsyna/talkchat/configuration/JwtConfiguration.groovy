package com.grytsyna.talkchat.configuration

import groovy.transform.CompileStatic

import javax.crypto.SecretKey
import java.util.function.Function
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import com.grytsyna.talkchat.entity.SecUser

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.JwtParserBuilder
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys

/**
* JwtConfiguration
*/
@CompileStatic
@Component
class JwtConfiguration {

    private static final String ROLE = 'role'

    @Value('${jwt.secret}')
    private String jwtSecret

    @Value('${jwt.expiration.ms}')
    private int jwtExpirationInMs

    String extractUserName(String token) {
        return extractClaim(token) { Claims claims -> claims.subject }
    }

    String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = [:]
        if (userDetails.is(SecUser)) {
            claims.put(ROLE, (userDetails as SecUser).role)
        }
        return makeToken(claims, userDetails.username)
    }

    boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token)
        return (userName == userDetails.username) && !isTokenExpired(token)
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private String makeToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(signingKey, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact()
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date())
    }

    private Date extractExpiration(String token) {
        return extractClaim(token) { Claims item -> item.expiration as Date }
    }

    private Claims extractAllClaims(String token) {
        JwtParserBuilder parserBuilder = Jwts.parser().setSigningKey(signingKey)
        JwtParser parser = parserBuilder.build()
        Claims claims = parser.parseClaimsJws(token).body
        return claims
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}
