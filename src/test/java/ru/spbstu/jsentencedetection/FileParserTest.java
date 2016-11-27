package ru.spbstu.jsentencedetection;

import junit.framework.TestCase;
import ru.spbstu.jsentencedetection.dispatcher.FileParser;

public class FileParserTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testValidate() throws Exception {
        final String fileName = "./src/test/resources/subjects.txt";
        assertEquals("physics", FileParser.parse(fileName).get(0));
    }
}
