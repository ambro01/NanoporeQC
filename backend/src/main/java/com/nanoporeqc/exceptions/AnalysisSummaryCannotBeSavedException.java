package com.nanoporeqc.exceptions;

public class AnalysisSummaryCannotBeSavedException extends RuntimeException{
    public AnalysisSummaryCannotBeSavedException() {
        super("Analysis summary cannot be saved");
    }
}
