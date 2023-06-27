package lab.ia.ExpenseManagement.Security.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${lab.security.jwtSecret}")
    private String jwtSecret;

    @Value("${lab.security.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    private byte[] getSecretKey() {
        return jwtSecret.getBytes(StandardCharsets.UTF_8);
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, getSecretKey())
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody().getSubject();
    }

    public Boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException signatureException) {
            logger.error("Invalid JWT signature: {}", signatureException.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            logger.error("Invalid JWT token: {}", malformedJwtException.getMessage());
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("JWT token is expired: {}", expiredJwtException.getMessage());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", unsupportedJwtException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            logger.error("JWT claims string is empty: {}", illegalArgumentException.getMessage());
        }
        return false;
    }
}
