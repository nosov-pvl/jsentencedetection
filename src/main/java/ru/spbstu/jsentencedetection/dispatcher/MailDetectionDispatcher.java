package ru.spbstu.jsentencedetection.dispatcher;

import org.apache.log4j.Logger;
import ru.spbstu.jsentencedetection.JSentenceDetection;
import ru.spbstu.jsentencedetection.TextPreprocessor;
import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailDetectionDispatcher implements DetectionDispatcher {
    private final static Logger logger = Logger.getLogger(MailDetectionDispatcher.class);

    private final MessagesLoader messagesLoader;
    private JSentenceDetection sentenceDetector;
    private TextPreprocessor preprocessor;

    public MailDetectionDispatcher(MessagesLoader messagesLoader) {
        this.messagesLoader = messagesLoader;
        preprocessor = new TextPreprocessor();

//        TODO: SparkContext here?

        sentenceDetector = new JSentenceDetection();
    }

    @Override
    public List<Boolean> detect(String filename) {
        if (!messagesLoader.load(filename)) {
            return null;
        }

        List<Message> messages = messagesLoader.getMessages();

        List<Boolean> verdicts = new ArrayList<>();
        for (Message message : messages) {
            logger.debug(message.getBody());
            String body = preprocessor.preprocess(message.getBody());
            logger.debug(body);

            verdicts.add(sentenceDetector.detect(Arrays.asList(body.split("[.\n]+")), message.getSubject()));
        }

        return verdicts;
    }

//     temporary method while Spark problem not solved
    public void closeContext() {
        try {
            sentenceDetector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
