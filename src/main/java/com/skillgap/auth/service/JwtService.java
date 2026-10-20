// package com.skillgap.auth.service;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Service;

// import javax.crypto.SecretKey;
// import java.nio.charset.StandardCharsets;
// import java.util.Date;
// import io.jsonwebtoken.Claims;

// @Service
// public class JwtService {

//     private static final String SECRET =
//             "SkillGapDevelopmentSecretKeyMustBeLongEnough123456789";

//     private static final long EXPIRATION_TIME =
//             1000L * 60 * 60 * 24;

//     private final SecretKey secretKey =
//             Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

//     public String generateToken(String userId, String email, String role) {

//         Date now = new Date();

//         return Jwts.builder()
//                 .subject(userId)
//                 .claim("email", email)
//                 .claim("role", role)
//                 .issuedAt(now)
//                 .expiration(new Date(now.getTime() + EXPIRATION_TIME))
//                 .signWith(secretKey)
//                 .compact();
//     }
//     public Claims validateToken(String token) {

//     return Jwts.parser()
//             .verifyWith(secretKey)
//             .build()
//             .parseSignedClaims(token)
//             .getPayload();
// }
// }


package com.skillgap.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME =
            1000L * 60 * 60 * 24;

    private final SecretKey secretKey;

    public JwtService(
            @Value("${JWT_SECRET:SkillGapDevelopmentSecretKeyMustBeLongEnough123456789}")
            String secret) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(String userId, String email, String role) {

        Date now = new Date();

        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
