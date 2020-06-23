package upcafe.security.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import upcafe.security.model.UserPrincipal;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    public String createToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        //TODO: why this number? Refactor to constant
        Date expiryDate = new Date(now.getTime() + 864000000);

        return Jwts.builder()
                .setSubject(Integer.toString(principal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, System.getenv("TOKEN_SIGNING_KEY"))
                .compact();
    }

    public int getIdFromToken(String token) {
        Claims claim = Jwts.parser()
                .setSigningKey(System.getenv("TOKEN_SIGNING_KEY"))
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claim.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(System.getenv("TOKEN_SIGNING_KEY")).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
