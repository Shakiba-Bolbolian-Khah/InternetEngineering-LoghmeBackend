package Loghme.PresentationController;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTmanager {
    private static JWTmanager instance;

    private String secretString = "loghmeloghmeloghmeloghmeloghmeloghmeloghme";
    private SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

    public static JWTmanager getInstance() {
        if(instance == null) {
            instance = new JWTmanager();
        }
        return instance;
    }

    public String createJWT(int userId, String email) {
        String issuer = "Corona";
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .claim("userId", userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public int validateJWT(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);
            return (int) claims.getBody().get("userId");
        } catch (JwtException e) {
            return -1;
        }
    }
}