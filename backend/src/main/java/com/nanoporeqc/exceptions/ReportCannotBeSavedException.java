package com.nanoporeqc.exceptions;

public class ReportCannotBeSavedException extends RuntimeException {
    public ReportCannotBeSavedException() {
        super("Html report cannot be save");
    }
}
