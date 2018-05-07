package com.nanoporeqc.fast5analyse.exceptions;

public class AnalyseNotFoundException extends Exception{
    public AnalyseNotFoundException() {
        super("Analyse not found");
    }
}
