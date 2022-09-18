package com.agenna.authPostgres.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "password_recovery", schema = "auth")
@Getter
@Setter
public class PasswordRecovery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(name = "date_insert")
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)    
    private User user;

    public PasswordRecovery(){
    }

    public PasswordRecovery(String token, User user){
        // this.user = user;
        this.token = token;
        this.expiredAt =  LocalDateTime.now();
    }
}
