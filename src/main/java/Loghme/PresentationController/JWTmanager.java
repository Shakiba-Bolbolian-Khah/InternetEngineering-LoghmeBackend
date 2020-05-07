package Loghme.PresentationController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JWTmanager {
    private static JWTmanager instance;

    private String secretString = "loghme";
    private String issuer = "Corona";

    public static JWTmanager getInstance() {
        if(instance == null) {
            instance = new JWTmanager();
        }
        return instance;
    }

    public String createJWT(int userId, String email) throws JWTCreationException{

        Algorithm algorithmHS = Algorithm.HMAC256(secretString);

        System.out.println("JWT maker: "+ email);

        String token = JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .withClaim("userId", userId)
                .sign(algorithmHS);

        System.out.println(token);
        return token;
    }

    public int validateJWT(String jwtString){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretString);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT jwt = verifier.verify(jwtString); //ToDo: check it!

            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("userId");
            int userId = claim.asInt();
            return userId;

        } catch (JWTVerificationException exception){
            return -1;
        }
    }
}
