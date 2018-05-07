package com.nanoporeqc.user.exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User not found");
    }
}
