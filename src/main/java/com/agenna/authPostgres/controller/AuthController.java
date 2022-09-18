package com.agenna.authPostgres.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agenna.authPostgres.dto.ForgotRequest;
import com.agenna.authPostgres.dto.ForgotResponse;
import com.agenna.authPostgres.dto.LoginRequest;
import com.agenna.authPostgres.dto.LoginResponse;
import com.agenna.authPostgres.dto.LogoutResponse;
import com.agenna.authPostgres.dto.RefreshResponse;
import com.agenna.authPostgres.dto.ResetRequest;
import com.agenna.authPostgres.dto.ResetResponse;
import com.agenna.authPostgres.dto.TwoFactorRequest;
import com.agenna.authPostgres.dto.TwoFactorResponse;
import com.agenna.authPostgres.dto.UpdateInfoRequest;
import com.agenna.authPostgres.dto.UpdatePasswordRequest;
import com.agenna.authPostgres.dto.UserRegisterRequest;
import com.agenna.authPostgres.dto.UserRegisterResponse;
import com.agenna.authPostgres.dto.UserResponse;
import com.agenna.authPostgres.entity.User;
import com.agenna.authPostgres.service.AuthService;
import com.agenna.authPostgres.service.Login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value="/register")
    public UserRegisterResponse register(@RequestBody UserRegisterRequest userRegister){
        
        logger.info("AntLog : AuthController register");
        
        User user = authService.register(
                userRegister.getFirstName(), 
                userRegister.getLastName(), 
                userRegister.getEmail(), 
                userRegister.getPassword(),
                userRegister.getPasswordConfirm());

        return new UserRegisterResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail());
    }

    @PostMapping(value="/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest,
                                HttpServletResponse response){
        
        Login loginTokens = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        
        Cookie cookie = new 
            Cookie("refresh_token", 
                    loginTokens.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");            
        response.addCookie(cookie);                            

        return new LoginResponse(
            loginTokens.getAccessToken().getUserId(),
            loginTokens.getOtpSecret(),
            loginTokens.getOtpAuthUrl());    
    }

    @GetMapping(value="/user")
    public UserResponse user(HttpServletRequest request){
        User user = (User)request.getAttribute("user");
        return new UserResponse(user);
    };

    @PutMapping(value="/user/info")
    public UserResponse userInfo(@RequestBody UpdateInfoRequest updateInfo){
        User user = authService.UpdateInfoUser(updateInfo);
        return new UserResponse(user);
    };

    @PostMapping(value="/refresh")
    public RefreshResponse refresh(@CookieValue("refresh_token") String refresh_token ){
        return new RefreshResponse(authService.refreshAccess(refresh_token).getAccessToken().getToken());
    }

    @PostMapping(value = "/logout")
    public LogoutResponse logout(@CookieValue("refresh_token") String refresh_token, HttpServletResponse response){
        authService.logout(refresh_token);

        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return new LogoutResponse("success");
    }

    @PostMapping(value ="/forgot")
    public ForgotResponse forgot(@RequestBody ForgotRequest forgotRequest, HttpServletRequest request){
        String originUrl = request.getHeader("Origin");
        
        authService.forgot(forgotRequest.getEmail(), originUrl);

        return new ForgotResponse("success");
    }

    @PostMapping(value="/reset")
    public ResetResponse reset(@RequestBody ResetRequest resetRequest){
        authService.reset(resetRequest.getToken(),
                          resetRequest.getPassword(),
                          resetRequest.getPasswordConfirm());
        return new ResetResponse("success");
    }

    @PostMapping(value="/two-factor")
    public TwoFactorResponse twoFactor(@RequestBody TwoFactorRequest twoFactorRequest){
        Login login = authService.twoFactorLogin(
            twoFactorRequest.getId(),
            twoFactorRequest.getSecret(),
            twoFactorRequest.getCode());
        return new TwoFactorResponse(login.getAccessToken().getToken());
    }

    @PutMapping(value="/user/password")
    public UserResponse userInfo(@RequestBody UpdatePasswordRequest updateInfo){

        User user = authService.UpdatePassword(updateInfo.getEmail(), updateInfo.getPassword(), updateInfo.getPasswordConfirm());
        return new UserResponse(user);
    };

}

