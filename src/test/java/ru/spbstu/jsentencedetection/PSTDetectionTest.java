package ru.spbstu.jsentencedetection;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Test;
import ru.spbstu.jsentencedetection.loaders.LoaderPST;
import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class PSTDetectionTest {
    @Test
    public void testPST1(){


        Logger logger = Logger.getLogger(PSTDetectionTest.class);
        logger.info("PSTDetectionTest start\n");

        JSentenceDetection detector = new JSentenceDetection();

        MessagesLoader loader = new LoaderPST();
        loader.load("src/test/resources/test.pst");

        List<Message> messages = loader.getMessages();

        int count_success = 0;
        for(Message m : messages) {

            if(!detector.detect(Arrays.asList(m.getBody().split("[.]")), m.getSubject())){
                logger.info("Passed " + m.getSubject());
                ++count_success;
            }
            else{
                logger.error("Failed " + m.getSubject());
                //logger.error(m.getBody());
            }
        }

        logger.info("" + count_success + "/" + messages.size() + " correct messages");

        try {
            detector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("PSTDetectionTest stop\n");

    }


}
