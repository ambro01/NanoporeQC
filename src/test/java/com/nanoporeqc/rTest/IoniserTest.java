package com.nanoporeqc.rTest;

import com.nanoporeqc.config.IntegrationTest;
import com.nanoporeqc.fast5.consts.FileConsts;
import com.nanoporeqc.r.config.RConfiguration;
import com.nanoporeqc.r.consts.RScriptsConst;
import com.nanoporeqc.r.domain.RVariable;
import com.nanoporeqc.r.service.RService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Category(IntegrationTest.class)
public class IoniserTest {

    private RConnection connection = null;
    private REngine engine = null;
    private RService rService;

    @Before
    public void createConnection() throws RserveException, IOException {
        connection = new RConnection();
        engine = connection;
        rService = new RService(connection, null);

        Map<String, File> fileMap = new HashMap<>();
        fileMap.put("1.fast5", new File(IoniserTest.class.getResource("/test/r_test/1.fast5").getFile()));
        fileMap.put("2.fast5", new File(IoniserTest.class.getResource("/test/r_test/2.fast5").getFile()));
        fileMap.put("3.fast5", new File(IoniserTest.class.getResource("/test/r_test/3.fast5").getFile()));
        fileMap.put("4.fast5", new File(IoniserTest.class.getResource("/test/r_test/4.fast5").getFile()));
        fileMap.put("5.fast5", new File(IoniserTest.class.getResource("/test/r_test/5.fast5").getFile()));

        fileMap.forEach((s, file) -> {
            String filePath = FileConsts.FAST5FILES_DIR + s;
            File destination = new File(filePath);
            try {
                if (destination.createNewFile()) {
                    FileUtils.copyFile(file, destination);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        connection.eval(String.format("source('%s')", getScriptFilePath(IoniserTest.class.getResource("/r_scripts/loadLibraries.R"))));
        connection.eval(String.format("source('%s')", getScriptFilePath(IoniserTest.class.getResource("/r_scripts/common.R"))));
        connection.eval(String.format("source('%s')", getScriptFilePath(IoniserTest.class.getResource("/r_scripts/readFast5SummaryFromDir.R"))));
    }

    @After
    public void closeConnection() {
        engine.close();
    }

    @Test
    public void testRScripts() {
        RScriptsConst.RScriptsMap.values().forEach(rScript -> {
            URL rScriptPath = RConfiguration.class.getResource("/r_scripts/" + rScript.getName().getFileName());

            try {
                connection.eval(String.format("source('%s')", getScriptFilePath(rScriptPath)));
            } catch (RserveException e) {
                e.printStackTrace();
            }

            List<RVariable> rVariables = new ArrayList<>(rScript.getRVariablesMap().values());

            for (RVariable rVariable : rVariables) {
                rService.updateRVariableData(rVariable);

                Assert.assertNotNull(rVariable.getRDataSet());
            }
        });
    }

    private String getScriptFilePath(URL url) {
        String filePath = FileConsts.SCRIPTS_DIR + "script.R";
        File source = new File(url.getFile());
        File destination = new File(filePath);

        if (destination.exists()) {
            destination.delete();
        }

        try {
            if (destination.createNewFile()) {
                FileUtils.copyFile(source, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destination.getAbsolutePath();
    }
}