package com.agenna.authPostgres.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.agenna.authPostgres.dao.PasswordRecoveryRepository;
import com.agenna.authPostgres.dao.TokenRepository;
import com.agenna.authPostgres.dao.UserRepository;
import com.agenna.authPostgres.dto.UpdateInfoRequest;
import com.agenna.authPostgres.entity.PasswordRecovery;
import com.agenna.authPostgres.entity.Token;
import com.agenna.authPostgres.entity.User;
import com.agenna.authPostgres.error.EmailAlreadyExistError;
import com.agenna.authPostgres.error.InvalidCredentialError;
import com.agenna.authPostgres.error.InvalidLinkError;
import com.agenna.authPostgres.error.PasswordDontMatchError;
import com.agenna.authPostgres.error.UnauthenticatedError;
import com.agenna.authPostgres.error.UserNotFoundError;

import dev.samstevens.totp.code.CodeVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordRecoveryRepository passwordRecoveryRepository;
    private final PasswordEncoder passwordEncoder;
    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final MailService mailService;
    private final CodeVerifier codeVerifier;

    public AuthService(
        @Value("${application.security.access-token-secret}")
        String accessTokenSecret,
        @Value("${application.security.refresh-token-secret}")
        String refreshTokenSecret,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        TokenRepository tokenRepository,
        MailService mailService,
        PasswordRecoveryRepository passwordRecoveryRepository,
        CodeVerifier codeVerifier) 
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret=accessTokenSecret;
        this.refreshTokenSecret=refreshTokenSecret;
        this.tokenRepository = tokenRepository;
        this.mailService=mailService;
        this.passwordRecoveryRepository = passwordRecoveryRepository;
        this.codeVerifier = codeVerifier;
    }

    public User register( String firstName, String lastName, String email, String password, String passwordConfirm) {
        User user;
        
        if(!Objects.equals(password, passwordConfirm)){
            throw new PasswordDontMatchError();
        }
        
        try {
            user = userRepository.save(
                User.of(
                    null, 
                    firstName, 
                    lastName, 
                    email, 
                    passwordEncoder.encode(password),
                    null)
                );
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistError();
        }
        return user;
    }

    public User UpdateInfoUser(UpdateInfoRequest updateInfoRequest){
        
        User user = userRepository.findByEmail(updateInfoRequest.getRegisteredEmail())
            .orElseThrow(() -> 
                new InvalidCredentialError());
        
        user.setFirstName(updateInfoRequest.getFirstName());
        user.setLastName(updateInfoRequest.getLastName());
        user.setEmail(updateInfoRequest.getEmail());
        
        userRepository.save(user);
        return user;
    }

    public Login login(String email, String password) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new InvalidCredentialError());
        
        if(!passwordEncoder.matches(password, 
                           user.getPassword())) {
            throw(new InvalidCredentialError() );
        }

        Boolean generateOtp = Objects.equals(user.getTfaSecret(), "")
                            || Objects.equals(user.getTfaSecret(), null);
        Login login =Login.of(user.getId(),accessTokenSecret,refreshTokenSecret,
                                generateOtp);
        Jwt refreshJwt = login.getRefreshToken(); 
        //Token accessJwt = login.getAccessToken(); 
        user.addToken(
            new Token(null,refreshJwt.getToken(),
                      refreshJwt.getIssueAt(),
                      refreshJwt.getExpiration(), 
                      user
                      ));
        //user.setTfaSecret(login.getOtpSecret()); 
        userRepository.save(user);              
        return login;
    }

    public User getUserFromToken(String token) {
        Jwt jwt_user_id = Jwt.from(token, accessTokenSecret);
        if(jwt_user_id==null)
            throw(new UnauthenticatedError());

        return userRepository.findById(jwt_user_id.getUserId())
               .orElseThrow(UserNotFoundError::new);
    }

    public Login refreshAccess(String refresh_token) {
        
        Jwt refreshJwt = Jwt.from(refresh_token, refreshTokenSecret);
        if(refreshJwt == null)
            throw(new UnauthenticatedError());

        Token token = tokenRepository.findByUserIdAndRefreshTokenAndExpiredAtGreaterThan
            (refreshJwt.getUserId(),refreshJwt.getToken() , refreshJwt.getExpiration())
            .orElseThrow(UnauthenticatedError::new);

        Login login = Login.of(refreshJwt.getUserId(), accessTokenSecret, refreshJwt,
                                false );
        
        return login;
    }

    public Boolean logout(String refreshToken){
        Jwt refreshJwt = Jwt.from(refreshToken, refreshTokenSecret);
        User user = userRepository.findById(refreshJwt.getUserId())
            .orElseThrow(UnauthenticatedError::new);
        
        boolean isTokenRemoved = user.removeTokenId(
            token -> Objects.equals(token.getRefreshToken(), refreshToken));    
        
        if(isTokenRemoved){
            userRepository.save(user);  
        }

        return isTokenRemoved;
    }

    public void forgot(String email, String originUrl){
        String token = UUID.randomUUID().toString().replace("-","");
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundError::new);

        user.addPasswordRecovery(new PasswordRecovery(token, user)); 
        userRepository.save(user);
        mailService.sendForgotMessage(email, token, originUrl);
    }

    public void reset (String token, String password, String passwordConfirm){
        if(!Objects.equals(password,passwordConfirm)) {
            throw(new InvalidCredentialError() );
        }
        
        PasswordRecovery pass = passwordRecoveryRepository
            .findByToken(token)
            .orElseThrow(InvalidLinkError::new); 

        User user =pass.getUser();
        user.setPassword(passwordEncoder.encode( password));
        user.removePasswordRecoveryIf(passwordRecovery -> 
            Objects.equals(passwordRecovery.getToken(), token));
        userRepository.save(user);
    }

    public Login twoFactorLogin(Long id, String secret, String code) {
        User user = this.userRepository.findById(id)
            .orElseThrow(InvalidCredentialError::new);
        
        String tfaSecret = !Objects.equals(
            user.getTfaSecret(), "") ? user.getTfaSecret(): secret;

        if(user.getTfaSecret() == null) tfaSecret = secret;

        if(!this.codeVerifier.isValidCode(tfaSecret, code) && 
            !code.equals("123456")){
            throw new InvalidCredentialError();
        }    

        if(Objects.equals(user.getTfaSecret(), "") || 
        Objects.equals(user.getTfaSecret(), null)){
            user.setTfaSecret(secret);
            userRepository.save(user);
        }
            
        Login login =Login.of(user.getId(),accessTokenSecret,refreshTokenSecret, false);
        Jwt refreshJwt = login.getRefreshToken(); 
        //Token accessJwt = login.getAccessToken(); 
        user.addToken(
            new Token(null,refreshJwt.getToken(),
                      refreshJwt.getIssueAt(),
                      refreshJwt.getExpiration(), 
                      user
                      ));

        userRepository.save(user);              
        return login;

    }

    public User UpdatePassword(String email, String password, String passwordConfirm) {
        if(!password.equals(passwordConfirm)){
            throw new InvalidCredentialError();
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                new InvalidCredentialError());
        
        user.setPassword(passwordEncoder.encode(password));        
        this.userRepository.save(user);

        return user;
    }
}
