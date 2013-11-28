package com.ontology2.centipede.parser;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"../shell/applicationContext.xml", "../testContext.xml"})
public class TestDirectoryExample {

    @Resource
    OptionParser directoryExampleParser;

    @Test
    public void substituteIntoLeft() throws IllegalAccessException {
        List<String> args= Lists.newArrayList(
            "-dir"
            ,"/var/left/"
            ,"-left"
            ,"chomsky,albert,zinn"
        );

        DirectoryExample ex=(DirectoryExample) directoryExampleParser.parse(args);
        List<String> expectedleft=Lists.newArrayList(
            new File("/var/left/chomsky").toString(),
            new File("/var/left/albert").toString(),
            new File("/var/left/zinn").toString()
        );

        assertEquals(expectedleft, ex.left);
    }
}
