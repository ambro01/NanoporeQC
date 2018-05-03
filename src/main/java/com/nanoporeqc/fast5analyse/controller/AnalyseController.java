package com.nanoporeqc.fast5analyse.controller;

import com.nanoporeqc.fast5analyse.dto.AnalyseDto;
import com.nanoporeqc.fast5analyse.service.AnalyseService;
import com.nanoporeqc.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/analyse")
public class AnalyseController {

    private final AnalyseService analyseService;

    @Autowired
    public AnalyseController(final AnalyseService analyseService) {
        this.analyseService = analyseService;
    }

    @PostMapping(value = "/new")
    public ResponseEntity runNewAnalyse(@RequestBody AnalyseDto analyseDto) throws IOException {
        analyseService.runNewAnalyse(analyseDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity runNewAnalyse(@PathVariable("id") Long id) {
        analyseService.runOldAnalyse(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/current-user")
    public List<AnalyseDto> getAnalysesForCurrentUser() throws UserNotFoundException {
        return analyseService.getAnalysesForCurrentUser();
    }

    @CrossOrigin
    @PostMapping(value = "/delete/{id}")
    public void deleteAnalyse(@PathVariable("id") Long id) {
        analyseService.deleteAnalyse(id);
    }
}