package com.agenna.authPostgres.service;

import dev.samstevens.totp.secret.DefaultSecretGenerator;
import lombok.Getter;

public class Login {
    @Getter
    private final Jwt accessToken;
    @Getter
    private final Jwt refreshToken;
    @Getter
    private final String otpSecret;
    @Getter
    private final String otpAuthUrl;

    private static final Long ACCESS_TOKEN_VALIDITY = 10L; 
    private static final Long REFRESH_TOKEN_VALIDITY = 1440L; 
    
    public Login(Jwt accessToken, Jwt refreshToken, String otpSecret, String otpAuthUrl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.otpSecret = otpSecret;
        this.otpAuthUrl = otpAuthUrl;
    }

    public static Login of(Long userId, String accessSecret, 
                           String refreshSecret, boolean generateOtp)
    {
        String otpSecret = null;
        String otpUrl = null;
        if(generateOtp){
            otpSecret = generateOtpSecret();
            otpUrl = getOtpUrl(otpSecret);
        }
        return new Login(
            Jwt.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
            Jwt.of(userId, REFRESH_TOKEN_VALIDITY, refreshSecret ),
            otpSecret, otpUrl );
    }

    public static Login of(Long userId, String accessSecret, 
                           Jwt refreshToken, boolean generateOtp)
    {
        String otpSecret = null;
        String otpUrl = null;
        if(generateOtp){
            otpSecret = generateOtpSecret();
            otpUrl = getOtpUrl(otpSecret);
        }
        return new Login(
            Jwt.of(userId, ACCESS_TOKEN_VALIDITY, accessSecret),
            refreshToken,
            otpSecret,otpUrl );
    }

    private static String generateOtpSecret(){
        return new DefaultSecretGenerator().generate();
        //return BaseEncoding.base32().encode(uuid.getBytes(StandardCharsets.UTF_8));
    }

    private static String getOtpUrl(String otpSecret){
        String appName="authApp";
        return String.format(
            "otpauth://totp/%s:Secret?secret=%s&issuer=%s", 
            appName,otpSecret,appName);
     }
}
