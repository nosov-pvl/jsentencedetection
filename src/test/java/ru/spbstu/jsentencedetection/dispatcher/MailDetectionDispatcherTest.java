package ru.spbstu.jsentencedetection.dispatcher;

import org.junit.Test;

import ru.spbstu.jsentencedetection.loaders.Message;
import ru.spbstu.jsentencedetection.loaders.MessagesLoader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MailDetectionDispatcherTest {
    @Test
    public void testDetect() throws IOException, ParserConfigurationException {
        MessagesLoader loaderMock = mock(MessagesLoader.class);
        when(loaderMock.load("aaa"))
                .thenReturn(true);

        when(loaderMock.getMessages())
                .thenReturn(new ArrayList<>(Arrays.asList(new Message(), new Message())));

        MailDetectionDispatcher dispatcher = new MailDetectionDispatcher(loaderMock);
        List<Boolean> res = dispatcher.detect("aaa");

        verify(loaderMock, times(1)).load("aaa");
        verify(loaderMock, times(1)).getMessages();

        assertEquals(2, res.size());
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
    }
}
