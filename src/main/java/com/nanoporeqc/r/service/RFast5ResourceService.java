package com.nanoporeqc.r.service;

import com.nanoporeqc.fast5.service.FileService;
import com.nanoporeqc.r.domain.RFast5Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class RFast5ResourceService {

    private final FileService fileService;

    @Autowired
    public RFast5ResourceService(final FileService fileService) {
        this.fileService = fileService;
    }
}
