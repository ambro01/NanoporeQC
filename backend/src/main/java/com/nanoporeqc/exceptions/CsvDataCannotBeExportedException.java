package com.nanoporeqc.exceptions;

public class CsvDataCannotBeExportedException extends RuntimeException{
    public CsvDataCannotBeExportedException() {
        super("CSV data cannot be exported");
    }
}
