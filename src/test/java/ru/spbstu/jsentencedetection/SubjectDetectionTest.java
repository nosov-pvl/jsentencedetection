package ru.spbstu.jsentencedetection;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.junit.Test;
import org.junit.Before;
import ru.spbstu.jsentencedetection.loaders.LoaderPST;
import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SubjectDetectionTest {
    @Test
    public void testFraud() {
        Logger logger = Logger.getLogger(PSTDetectionTest.class);
        logger.info("PSTDetectionTest start\n");

        JSentenceDetection detector = new JSentenceDetection();
        SubjectDetection subjectDetector = new SubjectDetection("fraudSubject", detector);

        MessagesLoader loader = new LoaderPST();
        loader.load("src/test/resources/test.pst");

        List<Message> messages = loader.getMessages();

        int count_success = 0;
        for(Message m : messages) {

            if(!subjectDetector.detect(Arrays.asList(m.getBody().split("[.]")))){
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
