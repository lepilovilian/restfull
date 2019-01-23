package com.ilepilov.restfull.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

    public static long EXPRIRATION_TIME;

    public static String TOKEN_PREFIX;

    public static String HEADER_STRING;

    public static String SIGN_UP_URL;

    public static String TOKEN_SECRET;

    @Value("${token_expiration_time}")
    public void setExprirationTime(long exprirationTime) {
        EXPRIRATION_TIME = exprirationTime;
    }

    @Value("${token_prefix}")
    public void setTokenPrefix(String tokenPrefix) {
        TOKEN_PREFIX = tokenPrefix;
    }

    @Value("${header_string}")
    public void setHeaderString(String headerString) {
        HEADER_STRING = headerString;
    }

    @Value("${sign_up_url}")
    public void setSignUpUrl(String signUpUrl) {
        SIGN_UP_URL = signUpUrl;
    }

    @Value("${token_secret}")
    public void setTokenSecret(String tokenSecret) {
        TOKEN_SECRET = tokenSecret;
    }
}
