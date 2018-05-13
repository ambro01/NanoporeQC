package com.nanoporeqc.fast5analysis.exceptions;

public class AnalysisNotFoundException extends Exception{
    public AnalysisNotFoundException() {
        super("Analysis not found");
    }
}
