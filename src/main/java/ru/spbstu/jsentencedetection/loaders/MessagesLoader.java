package ru.spbstu.jsentencedetection.loaders;

import java.util.List;

public interface MessagesLoader {
    public boolean load(String fileName);
    public List<Message> getMessages();
}
