package com.nanoporeqc.r.domain;

import lombok.Data;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RFast5Resource {

    private List<URL> filesUrls;

}
