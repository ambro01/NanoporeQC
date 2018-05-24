package com.nanoporeqc.exceptions;

public class UploadedFilesCannotBeSavedException extends RuntimeException{
    public UploadedFilesCannotBeSavedException() {
        super("Uploaded files cannot be saved");
    }
}
