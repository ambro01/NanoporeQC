package com.nanoporeqc.exceptions;

public class NotSupportedAnalysisTypeException extends RuntimeException {
    public NotSupportedAnalysisTypeException() {
        super("Analysis type is not found");
    }
}
