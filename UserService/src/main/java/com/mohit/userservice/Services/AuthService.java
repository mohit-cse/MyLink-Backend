package com.mohit.userservice.Services;

import com.mohit.userservice.DTOs.AuthRequestDTO;
import com.mohit.userservice.DTOs.AuthResponseDTO;
import com.mohit.userservice.Data.AuthToken;
import com.mohit.userservice.Data.AuthTokenRepository;
import com.mohit.userservice.Properties.AuthProperties;
import com.mohit.userservice.Utils.Exceptions.NoSuchTokenException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    private UUID createRandomUUID() {
        UUID token;
        do {
            token = UUID.randomUUID();
        } while (repository.existsByToken(token));
        return token;
    }

    @Transactional
    private Optional<UUID> isSessionActive(String userId){
        Optional<AuthToken> tokenOptional = repository.findById(userId);
        if(tokenOptional.isEmpty()) return Optional.empty();
        AuthToken token = tokenOptional.get();
        if(System.currentTimeMillis() >= token.getValidUntil()){
            repository.delete(token);
            return Optional.empty();
        }
        return Optional.of(token.getToken());
    }

    @Transactional
    public UUID generateToken(String userId) {
        repository.deleteById(userId);
        UUID token = createRandomUUID();
        repository.save(new AuthToken(userId, token, System.currentTimeMillis() + authProperties.getTokenValidity()));
        return token;
    }

    private UUID findToken(String userId) throws NoSuchTokenException{
        Optional<UUID> authTokenOptional = isSessionActive(userId);
        if(authTokenOptional.isEmpty()) throw new NoSuchTokenException("Session for with userID: " + userId + " doesn't exist");
        return authTokenOptional.get();
    }

    public ResponseEntity<AuthResponseDTO> authenticate(AuthRequestDTO authRequestDTO) {
        try{
            UUID token = findToken(authRequestDTO.userId());
            if(!token.equals(authRequestDTO.token()))
                return new ResponseEntity<>(new AuthResponseDTO(false, authRequestDTO.userId()), HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(new AuthResponseDTO(true, authRequestDTO.userId()), HttpStatus.OK);
        }
        catch (NoSuchTokenException e){
            e.printStackTrace();
            return new ResponseEntity<>(new AuthResponseDTO(false, authRequestDTO.userId()), HttpStatus.NOT_FOUND);
        }
    }
}
