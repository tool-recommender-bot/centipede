package com.ontology2.centipede.parser;

import com.google.common.collect.Lists;
import com.ontology2.centipede.shell.UnparsableDefaultException;
import com.ontology2.centipede.shell.UnparsableOptionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"../shell/applicationContext.xml", "../testContext.xml"})
public class TestParsing {
    @Resource
    OptionParser exampleOne;

    @Resource
    OptionParser invalidDefault;

    @Test
    public void testDefaultHandling() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(new ArrayList<String>());
        assertEquals(1234L, (long) ioe.john);
        assertEquals(false, ioe.badass);
    }

    @Test
    public void testCaseOne() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                new ArrayList<String>() {{
                    add("-johnny");
                    add("1234567890");
                }}
        );
        assertEquals(1234567890L, (long) ioe.john);
        assertEquals(false, ioe.badass);
    }

    @Test
    public void testCaseTwo() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                new ArrayList<String>() {{
                    add("-badass");
                    add("-johnny");
                    add("77");
                }}
        );
        assertEquals(77L, (long) ioe.john);
        assertEquals(true, ioe.badass);
        assertEquals(0,ioe.jimmy);
        assertTrue(ioe.numbers.isEmpty());
    }

    @Test
    public void assignToJimmy() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                new ArrayList<String>() {{
                    add("-jimmy");
                    add("419");
                }}
        );

        assertEquals(ioe.jimmy,419);
    }


    @Test
    public void unparsableOptionFailsAppropriately() throws IllegalAccessException {
        UnparsableOptionException blowup = null;
        try {
            InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                    new ArrayList<String>() {{
                        add("-johnny");
                        add("-badass");
                        add("421");
                    }}
            );
        } catch (UnparsableOptionException x) {
            blowup = x;
        }

        assertNotNull(blowup);
        assertEquals("johnny", blowup.getOptionName());
        assertEquals("-badass", blowup.getInvalidValue());
        assertEquals(Long.class, blowup.getTargetType());
    }

    @Test
    public void unparsableDefaultFailsAppropriately() throws IllegalAccessException {
        UnparsableDefaultException blowup = null;
        try {
            invalidDefault.parse(
                    new ArrayList<String>()
            );
        } catch (UnparsableDefaultException x) {
            blowup = x;
        }

        assertNotNull(blowup);
        assertEquals("burn", blowup.getOptionName());
        assertEquals("i am the king of hellfire", blowup.getInvalidValue());
        assertEquals(Integer.TYPE, blowup.getTargetType());
    }

    @Test
    public void parseNumbers() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                new ArrayList<String>() {{
                    add("-numbers");
                    add("2");
                    add("-numbers");
                    add("4");
                    add("-numbers");
                    add("6");
                    add("-numbers");
                    add("8");
                }}
        );

        List<Integer> rightAnswer=Lists.newArrayList(2,4,6,8);
        assertEquals(rightAnswer,ioe.numbers);
    }

    @Test
    public void parseCommaSeparatedNumbers() throws IllegalAccessException {
        InheritedOptionExample ioe = (InheritedOptionExample) exampleOne.parse(
                new ArrayList<String>() {{
                    add("-numbers");
                    add("3,5,7,9");
                }}
        );

        List<Integer> rightAnswer=Lists.newArrayList(3,5,7,9);
        assertEquals(rightAnswer,ioe.numbers);
    }
}
