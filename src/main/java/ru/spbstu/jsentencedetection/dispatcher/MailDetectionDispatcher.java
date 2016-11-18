package ru.spbstu.jsentencedetection.dispatcher;

import ru.spbstu.jsentencedetection.JSentenceDetection;
import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MailDetectionDispatcher implements DetectionDispatcher {
    private final MessagesLoader messagesLoader;
    private JSentenceDetection sentenceDetector;

    public MailDetectionDispatcher(MessagesLoader messagesLoader) {
        this.messagesLoader = messagesLoader;

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
            verdicts.add(sentenceDetector.detect(Arrays.asList(message.getBody().split("\\.")), message.getSubject()));
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
