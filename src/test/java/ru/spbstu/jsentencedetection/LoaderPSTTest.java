package ru.spbstu.jsentencedetection;

import org.junit.Test;
import ru.spbstu.jsentencedetection.loaders.LoaderPST;
import ru.spbstu.jsentencedetection.loaders.Message;

import java.util.List;

import static org.junit.Assert.*;

public class LoaderPSTTest {
    final String fileName = "./src/test/resources/test.pst";

    @Test
    public void test() {
        LoaderPST parserPST = new LoaderPST();
        parserPST.load(fileName);
        List<Message> messages = parserPST.getMessages();
        assertFalse(messages == null);
        assertFalse(messages.size() == 0);
        for(Message message : messages) {
            System.out.println(message);
        }
    }
}