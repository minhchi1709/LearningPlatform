package com.mchis.LearningPlatform.exceptions;

public class TokenExpiredException extends Exception{
    public TokenExpiredException() {
        super("Token expired or invalid");
    }
}
