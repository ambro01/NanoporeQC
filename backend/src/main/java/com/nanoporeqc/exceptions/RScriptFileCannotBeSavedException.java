package com.nanoporeqc.exceptions;

public class RScriptFileCannotBeSavedException extends RuntimeException{
    public RScriptFileCannotBeSavedException() {
        super("R script file cannot be saved");
    }
}
