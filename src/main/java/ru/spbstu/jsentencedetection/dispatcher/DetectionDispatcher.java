package ru.spbstu.jsentencedetection.dispatcher;

import java.util.List;

public interface DetectionDispatcher {
    List<Boolean> detect(String filename);
}
