package com.agenna.authPostgres.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agenna.authPostgres.entity.PasswordRecovery;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long>{

    public Optional<PasswordRecovery> 
    findByUserIdAndToken
        (Long userid, String token);  

    public Optional<PasswordRecovery> 
    findByToken
        (String token);  
}
