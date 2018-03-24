package com.nanoporeqc.r.services;

import com.nanoporeqc.r.domain.RFast5Resource;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class RFast5ResourceService {

    // create paths list, eg. c('data/a.fast5', 'data/b.fast5)
    public String getFilesPathsAsString(RFast5Resource rFast5Resource) {
        StringBuilder sb = new StringBuilder();
        sb.append("c(");

        List<String> filesPaths = rFast5Resource.getFilesUrls()
                .stream()
                .map(url -> {
                    try {
                        return Paths.get(url.toURI()).toFile().getAbsolutePath();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        for (String filePath : filesPaths) {
            sb.append("'");
            sb.append(filePath);
            sb.append("'");
            sb.append(", ");
        }
        sb.delete(sb.lastIndexOf(","), sb.length());
        sb.append(")");

        return sb.toString();
    }
}
