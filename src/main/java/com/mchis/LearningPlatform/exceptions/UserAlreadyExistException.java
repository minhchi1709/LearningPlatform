package com.mchis.LearningPlatform.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserAlreadyExistException extends Exception{

    public UserAlreadyExistException() {
        super("Username or email already exists!");
    }
}
