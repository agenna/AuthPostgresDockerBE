package com.agenna.authPostgres.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agenna.authPostgres.entity.User;
import com.agenna.authPostgres.error.UnauthenticatedError;
import com.agenna.authPostgres.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor{

    private AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception 
    {
        logger.info("AntLog : AuthInterceptor preHandle");

        String authHeader = request.getHeader("Authorization");
        if(authHeader== null || 
           !authHeader.startsWith("Bearer") ||
           authHeader.length() < 8
           ){
                throw new UnauthenticatedError();
        }

        try{ 
            User user = authService.getUserFromToken(authHeader.substring(7));
            request.setAttribute("user",user);
        }
        catch(Exception e){
            throw(e);
        }
        return true;
    }
}
