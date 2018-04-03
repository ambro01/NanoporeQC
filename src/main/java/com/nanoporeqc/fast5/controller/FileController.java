package com.nanoporeqc.fast5.controller;

import com.nanoporeqc.fast5.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @CrossOrigin
    @PostMapping
    public ResponseEntity getFiles(@RequestParam MultipartFile[] files) throws IOException {
        fileService.saveFiles(files);
        return new ResponseEntity(HttpStatus.OK);
    }
}
