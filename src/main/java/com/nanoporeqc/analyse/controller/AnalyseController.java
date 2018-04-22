package com.nanoporeqc.analyse.controller;

import com.nanoporeqc.analyse.dto.AnalyseDto;
import com.nanoporeqc.analyse.service.AnalyseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/analyse")
public class AnalyseController {

    @Autowired
    private AnalyseService analyseService;

    @CrossOrigin
    @PostMapping(value = "/new")
    public ResponseEntity runNewAnalyse(@RequestBody AnalyseDto analyseDto) throws IOException {
        analyseService.runNewAnalyse(analyseDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}