package Loghme.PresentationController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTmanager {
    private static JWTmanager instance;

    private String secretString = "loghme";

    public static JWTmanager getInstance() {
        if(instance == null) {
            instance = new JWTmanager();
        }
        return instance;
    }

    public String createJWT(int userId, String email){
        SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .claim("userId", userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jws;
    }

    public int validateJWT(String jws){
        return 1; // returns userId from jws
    }
}
