package com.nanoporeqc;

import com.nanoporeqc.config.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(IntegrationTest.class)
public class fileTest {

    @Test
    public void savingTest() throws IOException {
        String filePath = "/tmp/testFiles/a.txt";

        File file = new File(filePath);

        boolean isCreated = file.createNewFile();

        Assert.assertTrue(isCreated);
    }
}
