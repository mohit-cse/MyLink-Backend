package com.mohit.mylink.Services;

import com.mohit.mylink.Data.AuthToken;
import com.mohit.mylink.Data.AuthTokenRepository;
import com.mohit.mylink.Properties.AuthProperties;
import com.mohit.mylink.Utils.Generator.UserIDGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthTokenRepository repository;
    private final AuthProperties authProperties;
    AuthService(AuthTokenRepository repository, AuthProperties authProperties){
        this.repository = repository;
        this.authProperties = authProperties;
    }

    private UUID createRandomUUID() {
        UUID token;
        do {
            token = UUID.randomUUID();
        } while (repository.existsByToken(token));
        return token;
    }

    Optional<UUID> isSessionActive(String userId){
        Optional<AuthToken> tokenOptional = repository.findById(userId);
        if(tokenOptional.isEmpty()) return Optional.empty();
        AuthToken token = tokenOptional.get();
        if(System.currentTimeMillis() >= token.getValidUntil()){
            repository.delete(token);
            return Optional.empty();
        }
        return Optional.of(token.getToken());
    }

    public UUID generateToken(String userId) {
        UUID token = createRandomUUID();
        repository.save(new AuthToken(userId, token, System.currentTimeMillis() + authProperties.getTokenValidity()));
        return token;
    }
}
