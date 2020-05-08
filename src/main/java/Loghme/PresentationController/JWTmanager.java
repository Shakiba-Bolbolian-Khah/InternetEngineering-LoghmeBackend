package Loghme.PresentationController;

import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTmanager {
    private static JWTmanager instance;

    private String secretString = "loghmeloghmeloghmeloghmeloghmeloghmeloghme";
    private SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));

    private class JWTdata {
        private String JWT;
        private Date expDate;

        public JWTdata(String JWT, Date expDate) {
            this.JWT = JWT;
            this.expDate = expDate;
        }
    }
    public static JWTmanager getInstance() {
        if(instance == null) {
            instance = new JWTmanager();
        }
        return instance;
    }

    public String createJWT(int userId) {
        String issuer = "Corona";
        Date expDate = new Date(System.currentTimeMillis() + 60 * 1000);
        String jwtStr = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .claim("userId", userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return new Gson().toJson(new JWTdata(jwtStr, expDate));
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