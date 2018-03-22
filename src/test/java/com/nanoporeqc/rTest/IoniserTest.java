package com.nanoporeqc.rTest;

import com.nanoporeqc.config.IntegrationTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Category(IntegrationTest.class)
public class IoniserTest {

    private RConnection connection = null;

    private REngine engine = null;

    @Before
    public void createConnection() throws RserveException, IOException {
        connection = new RConnection();
        engine = connection;

        List<URL> urlList = new ArrayList<>();
        urlList.add(IoniserTest.class.getResource("/test/r_test/1.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/2.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/3.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/4.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/5.fast5"));

        String filesPath = getFilesList(urlList);

        ClassPathResource rScript = new ClassPathResource("r_scripts/loadLibraries.R");
        connection.eval(String.format("source('%s')", rScript.getFile().getAbsolutePath()));
        connection.eval("files <- " + filesPath);
        rScript = new ClassPathResource("r_scripts/readFast5Summary.R");
        connection.eval(String.format("source('%s')", rScript.getFile().getAbsolutePath()));
    }

    @After
    public void closeConnection() {
        engine.close();
    }

    @Test
    public void testReadFast5Summary() throws IOException, RserveException, REXPMismatchException {
        ClassPathResource rScript = new ClassPathResource("r_scripts/readAccumulation.R");

        connection.eval(String.format("source('%s')", rScript.getFile().getAbsolutePath()));

        double[][] arrayData = connection.eval("matrixData").asDoubleMatrix();
        double[] minutes = connection.eval("minutes").asDoubles();
        double[] newReads = connection.eval("newReads").asDoubles();
        double[] accumulation = connection.eval("accumulation").asDoubles();

        Assert.assertEquals(5, minutes.length);
        Assert.assertEquals(5, newReads.length);
        Assert.assertEquals(5, accumulation.length);
    }

    private String getFilesList(List<URL> urlList) {
        StringBuilder sb = new StringBuilder();
        sb.append("c(");

        List<String> filesPaths = urlList
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