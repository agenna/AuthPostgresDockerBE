package com.agenna.authPostgres.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user", schema = "auth")
@Getter
@Setter

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="tfa_secret")
    private String tfaSecret;

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL, mappedBy = "user")
    Set<Token> tokens = new HashSet<>();

    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL, mappedBy = "user")
    Set<PasswordRecovery> passwordRecoveries = new HashSet<>();

    public User() {
    }

    public static User of(Long id, String firstName, String lastName, String email, String password, String tfaSecret) {
        return new User(null, firstName,lastName,email,password,null, Collections.emptySet(),Collections.emptySet() );    
    } 

    private User(Long id, String firstName, String lastName, String email, String password, String tfaSecret,
                 Collection<Token> tokens, Collection<PasswordRecovery> passwordRecoveries) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.tfaSecret = tfaSecret;
        this.tokens.addAll(tokens);
        this.passwordRecoveries.addAll(passwordRecoveries);
    } 

    public void addToken(Token token){
        this.tokens.add(token);
    }

    public boolean removeToken(Token token){
        return this.tokens.remove(token);
    }

    public boolean removeTokenId(Predicate<? super Token> predicate ){
        return this.tokens.removeIf(predicate);
    }

    public void addPasswordRecovery(PasswordRecovery passwordRecovery){
        this.passwordRecoveries.add(passwordRecovery);
    }

    public boolean removePasswordRecovery(PasswordRecovery passwordRecovery){
        return this.passwordRecoveries.remove(passwordRecovery);
    }

    public boolean removePasswordRecoveryIf(Predicate<? super PasswordRecovery> predicate ){
        return this.passwordRecoveries.removeIf(predicate);
    }
}
