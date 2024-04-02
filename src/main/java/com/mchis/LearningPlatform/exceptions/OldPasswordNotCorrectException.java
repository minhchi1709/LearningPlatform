package com.mchis.LearningPlatform.exceptions;

public class OldPasswordNotCorrectException extends Exception{

    public OldPasswordNotCorrectException() {
        super("Old password not correct");
    }
}
