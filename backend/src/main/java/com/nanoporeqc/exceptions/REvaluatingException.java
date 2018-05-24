package com.nanoporeqc.exceptions;

public class REvaluatingException extends RuntimeException {
    public REvaluatingException() {
        super("Evaluating R command failed");
    }
}
