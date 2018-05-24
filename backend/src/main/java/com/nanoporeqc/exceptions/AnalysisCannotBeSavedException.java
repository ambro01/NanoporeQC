package com.nanoporeqc.exceptions;

public class AnalysisCannotBeSavedException extends RuntimeException{
    public AnalysisCannotBeSavedException() {
        super("Analysis cannot be saved");
    }
}
