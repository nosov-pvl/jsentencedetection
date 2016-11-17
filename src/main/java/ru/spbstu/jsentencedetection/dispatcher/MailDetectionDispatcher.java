package ru.spbstu.jsentencedetection.dispatcher;

import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import java.util.ArrayList;
import java.util.List;

public class MailDetectionDispatcher implements DetectionDispatcher {
    private final MessagesLoader messagesLoader;

    public MailDetectionDispatcher(MessagesLoader messagesLoader) {
        this.messagesLoader = messagesLoader;

//        TODO: SparkContext here?
    }

    @Override
    public List<Boolean> detect(String filename) {
        if (!messagesLoader.load(filename)) {
            return null;
        }

        List<Message> messages = messagesLoader.getMessages();

//        TODO: add SentenceDetector handler
        List<Boolean> res = new ArrayList<>();
        for (Message message : messages) {
            res.add(false);
        }

        return res;
    }
}
