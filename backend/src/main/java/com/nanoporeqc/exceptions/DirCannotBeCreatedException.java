package com.nanoporeqc.exceptions;

public class DirCannotBeCreatedException extends RuntimeException {
    public DirCannotBeCreatedException() {
        super("Dir cannot be created");
    }
}
