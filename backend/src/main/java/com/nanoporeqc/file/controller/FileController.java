package com.nanoporeqc.file.controller;

import com.nanoporeqc.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity saveFiles(@RequestParam final MultipartFile[] files) {
        fileService.saveFiles(files);
        return new ResponseEntity(HttpStatus.OK);
    }
}
