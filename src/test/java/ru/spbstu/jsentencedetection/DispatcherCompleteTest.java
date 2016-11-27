package ru.spbstu.jsentencedetection;

import org.apache.log4j.Logger;
import org.junit.Test;
import ru.spbstu.jsentencedetection.dispatcher.MailDetectionDispatcher;
import ru.spbstu.jsentencedetection.loaders.LoaderPST;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DispatcherCompleteTest {
    private final static Logger logger = Logger.getLogger(DispatcherCompleteTest.class);

    @Test
    public void testDetect() throws IOException, ParserConfigurationException {
        final String PST_FILENAME = "./src/test/resources/test.pst";

        LoaderPST loaderPST = new LoaderPST();

        MailDetectionDispatcher dispatcher = new MailDetectionDispatcher(loaderPST);
        List<Boolean> res = dispatcher.detect(PST_FILENAME);

        assertTrue(res.size() != 0);

        String res_string = "";
        for (boolean verdict : res) {
            res_string += verdict + " ";
        }

        logger.info(res_string);

        dispatcher.closeContext();
    }
}
