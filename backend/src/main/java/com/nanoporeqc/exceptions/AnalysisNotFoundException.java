package com.nanoporeqc.exceptions;

public class AnalysisNotFoundException extends RuntimeException {
    public AnalysisNotFoundException() {
        super("Analysis not found");
    }
}
