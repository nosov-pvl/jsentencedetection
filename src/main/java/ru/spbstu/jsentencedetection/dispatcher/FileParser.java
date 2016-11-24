package ru.spbstu.jsentencedetection.dispatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileParser {
    //Text file have to content subject for analyse
    public static List<String> parse(String fileName) throws IOException{
        List<String> result = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        return result;
    }
}
