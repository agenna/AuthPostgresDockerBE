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
@Table(name="token", schema = "auth")
@Getter
@Setter
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)    
    private User user;

    public Token() {
    }

    public static Token of( String refresh_token, LocalDateTime issued_at, LocalDateTime expired_at, User user){
        return new Token( null, refresh_token, issued_at,expired_at, user);
    }

    public Token(Long id, String refresh_token, LocalDateTime issued_at, LocalDateTime expired_at, User user) {
        this.id = id;
        this.refreshToken = refresh_token;
        this.expiredAt = expired_at;
        this.issuedAt = issued_at;
        this.user = user;
    }
}
