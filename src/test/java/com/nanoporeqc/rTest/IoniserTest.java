package com.nanoporeqc.rTest;

import com.nanoporeqc.config.IntegrationTest;
import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RFast5Resource;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.service.RFast5ResourceService;
import com.nanoporeqc.r.service.RService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Category(IntegrationTest.class)
public class IoniserTest {

    private RConnection connection = null;
    private REngine engine = null;
    private RFast5Resource rFast5Resource;
    private RService rService;

    @Before
    public void createConnection() throws RserveException, IOException {
        connection = new RConnection();
        engine = connection;
        rFast5Resource = new RFast5Resource();
        rService = new RService(connection);
        RFast5ResourceService rFast5ResourceService = new RFast5ResourceService();

        List<URL> urlList = new ArrayList<>();
        urlList.add(IoniserTest.class.getResource("/test/r_test/1.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/2.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/3.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/4.fast5"));
        urlList.add(IoniserTest.class.getResource("/test/r_test/5.fast5"));

        rFast5Resource.setFilesUrls(urlList);

        String filesPath = rFast5ResourceService.getFilesPathsAsString(rFast5Resource);

        ClassPathResource rScript = new ClassPathResource("r_scripts/loadLibraries.R");
        connection.eval(String.format("source('%s')", rScript.getFile().getAbsolutePath()));
        rScript = new ClassPathResource("r_scripts/common.R");
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
    public void testRScripts() {
        RScriptsConst.RScriptsMap.values().forEach(rScript -> {
            ClassPathResource rScriptPath = new ClassPathResource("r_scripts/" + rScript.getName().getFileName());

            try {
                connection.eval(String.format("source('%s')", rScriptPath.getFile().getAbsolutePath()));
            } catch (RserveException | IOException e) {
                e.printStackTrace();
            }

            List<RVariable> rVariables = new ArrayList<>(rScript.getRVariablesMap().values());

            for (RVariable rVariable : rVariables) {
                rVariable.setRDataSet(rService.getDataSetFromR(rVariable));

                Assert.assertNotNull(rVariable.getRDataSet());
            }
        });
    }
}