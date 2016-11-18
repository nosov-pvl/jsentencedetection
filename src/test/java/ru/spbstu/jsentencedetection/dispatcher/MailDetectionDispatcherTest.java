package ru.spbstu.jsentencedetection.dispatcher;

import org.junit.Test;

import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MailDetectionDispatcherTest {
    @Test
    public void testDetect() throws IOException, ParserConfigurationException {
        MessagesLoader loaderMock = mock(MessagesLoader.class);
        when(loaderMock.load("aaa"))
                .thenReturn(true);

        Message message = createMessage();

        when(loaderMock.getMessages())
                .thenReturn(new ArrayList<>(Arrays.asList(message)));

        MailDetectionDispatcher dispatcher = new MailDetectionDispatcher(loaderMock);
        List<Boolean> res = dispatcher.detect("aaa");

        verify(loaderMock, times(1)).load("aaa");
        verify(loaderMock, times(1)).getMessages();

        assertTrue(res.equals(Arrays.asList(true)));

        dispatcher.closeContext();
    }

    @Test
    public void testLoadFailedDetect() {
        MessagesLoader loaderMock = mock(MessagesLoader.class);
        when(loaderMock.load("bbb"))
                .thenReturn(false);

        when(loaderMock.getMessages())
                .thenReturn(new ArrayList<>(Arrays.asList(new Message(), new Message())));

        MailDetectionDispatcher dispatcher = new MailDetectionDispatcher(loaderMock);
        List<Boolean> res = dispatcher.detect("bbb");

        verify(loaderMock, times(1)).load("bbb");
        verify(loaderMock, never()).getMessages();

        assertEquals(null, res);

        dispatcher.closeContext();
    }

    Message createMessage() {
        Scanner s = null;
        try {
            s = new Scanner(new File("src/main/resources/oil.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String messageBody = "";
        while (s.hasNextLine()){
            messageBody += s.nextLine();
        }
        s.close();


        Message message = new Message();
        message.setSubject("Кредитный рейтинг России в 2016 году достигнет рекордных величин.");
        message.setBody(messageBody);

        return message;
    }
}
