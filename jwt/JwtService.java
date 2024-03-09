package cgg.blogapp.blogapp.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;

@Service
public class JwtService {

    public final String secret = "usernamedjqsbubqsuqucuvbcusbucbasubxsbxjabxjbasjxbasjbx";

    public String generateJWTToken(String username) {

        System.out.println(" ---- GENERATING-TOKENS ---- ");

        Map<String, Object> claims = new HashMap<>();

        return createToken(username, claims);

    }

    @SuppressWarnings("deprecation")
    public String createToken(String username, Map<String, Object> claims) {

        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2000 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secret key
    @SuppressWarnings("deprecation")
    private Claims getAllClaimsFromToken(String token) {
        System.out.println("in claims ________________________" + token);
        // return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getPayload();

    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
