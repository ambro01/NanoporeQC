package com.nanoporeqc;

import com.nanoporeqc.config.IntegrationTest;
import com.nanoporeqc.file.consts.FileConsts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Category(IntegrationTest.class)
public class fileTest {

    @Test
    public void savingTest() throws IOException {
        String filePath = "/home/ra/testFiles/a.txt";

        File file = new File(filePath);

        boolean isCreated = file.createNewFile();

        Assert.assertTrue(isCreated);
    }

    @Test
    public void testFastQcTool() {
        try {
            final List<String> filesPaths = Files.list(Paths.get("/home/ra/q/"))
                    .map(Path::toAbsolutePath)
                    .map(Object::toString)
                    .collect(Collectors.toList());
            final Process process = Runtime.getRuntime().exec("fastqc" + getConnectedFilesPaths(filesPaths));
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getConnectedFilesPaths(final List<String> filesPaths) {
        final StringBuilder sb = new StringBuilder();
        for (String path : filesPaths) {
            sb.append(" ").append(path);
        }
        return sb.toString();
    }
}
