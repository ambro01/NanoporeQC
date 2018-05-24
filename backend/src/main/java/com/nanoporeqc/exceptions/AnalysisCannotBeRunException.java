package com.nanoporeqc.exceptions;

public class AnalysisCannotBeRunException extends RuntimeException {
    public AnalysisCannotBeRunException() {
        super("Analysis cannot be run");
    }
}
