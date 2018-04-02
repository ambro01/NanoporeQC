package com.nanoporeqc.analyse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/analyse")
public class AnalyseController {

    @Autowired
    private AnalyseService analyseService;

    @CrossOrigin
    @PostMapping(value = "/new")
    public ResponseEntity runNewAnalyse(@RequestParam String analyseName) throws IOException {
        analyseService.runNewAnalyse(analyseName);
        return new ResponseEntity(HttpStatus.OK);
    }
}