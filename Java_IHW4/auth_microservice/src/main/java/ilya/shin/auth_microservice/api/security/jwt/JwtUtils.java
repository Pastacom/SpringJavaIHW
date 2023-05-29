package ilya.shin.auth_microservice.api.security.jwt;

import ilya.shin.auth_microservice.api.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${kpo.app.jwtSecret}")
    private String jwtSecret;

    @Value("${kpo.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Boolean validateToken(String jwt) throws Exception {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            System.err.println(malformedJwtException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            System.err.println(illegalArgumentException.getMessage());
        }

        return false;
    }

    public String getEmailFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }

    public Date getExpirationTimeFromJwtToken(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getExpiration();
    }
}
