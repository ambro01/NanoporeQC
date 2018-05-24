package com.nanoporeqc.exceptions;

public class RScriptFileCannotBeSavedException extends RuntimeException{
    public RScriptFileCannotBeSavedException() {
        super("Rm script file cannot be saved");
    }
}
