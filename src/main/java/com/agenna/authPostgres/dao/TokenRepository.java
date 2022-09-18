package com.agenna.authPostgres.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agenna.authPostgres.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

    public Optional<Token> 
        findByUserIdAndRefreshTokenAndExpiredAtGreaterThan
            (Long userid, String refreshToken, LocalDateTime expiredAt );  

    
/*     public Optional<Token> 
        findByUserIdAndRefreshTokenAndExpiredAtLessThan
            (Long userid, String refreshToken, LocalDateTime expiredAt );  

    public Optional<Token> 
        findByUserIdAndRefreshToken
            (Long userid, String refreshToken );  */ 
    
}
