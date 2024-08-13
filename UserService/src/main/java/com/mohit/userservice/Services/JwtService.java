package com.mohit.userservice.Services;

import com.mohit.userservice.DTOs.AuthRequestDTO;
import com.mohit.userservice.DTOs.AuthResponseDTO;
import com.mohit.userservice.DTOs.LoginRequestDTO;
import com.mohit.userservice.Data.UserProfile;
import com.mohit.userservice.Facade.UserProfileFacade;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private final UserDetailsService userDetailsService;
    private final UserProfileFacade userProfileFacade;

    public JwtService(
            UserDetailsService userDetailsService,
            UserProfileFacade userProfileFacade
    ) {
        this.userDetailsService = userDetailsService;
        this.userProfileFacade = userProfileFacade;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public ResponseEntity<AuthResponseDTO> authenticateToken(AuthRequestDTO authRequestDTO) {
        String userEmail = extractUsername(authRequestDTO.jwtToken());
        if(userEmail == null )
            return new ResponseEntity<>(new AuthResponseDTO(false, null), HttpStatus.UNAUTHORIZED);
        UserProfile userProfile = userProfileFacade.getProfileByEmail(userEmail);
        if(userProfile == null)
            return new ResponseEntity<>(new AuthResponseDTO(false, null), HttpStatus.UNAUTHORIZED);
        if (!isTokenValid(authRequestDTO.jwtToken(), userEmail))
            return new ResponseEntity<>(new AuthResponseDTO(false, null), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(new AuthResponseDTO(true, userProfile.getUserId()), HttpStatus.OK);
    }
}
