package ru.spbstu.jsentencedetection;

import junit.framework.TestCase;
import org.junit.Test;
import ru.spbstu.jsentencedetection.JSentenceDetection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class JSentenceDetectionTest {
    @Test
    public void testRussian(){

        Scanner s = null;
        try {
            s = new Scanner(new File("src/main/resources/oil.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();

        JSentenceDetection detector = new JSentenceDetection();
        assertFalse(detector.detect(list, "У людей, страдающих гипертонией, давление может резко и сильно вырасти до " +
                "критических значений, угрожающих здоровью."));
        TestCase.assertTrue(detector.detect(list, "Кредитный рейтинг России в 2016 году достигнет рекордных величин."));
        try {
            detector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEnglish(){

        Scanner s = null;
        try {
            s = new Scanner(new File("src/main/resources/drones.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<String>();
        while (s.hasNextLine()){
            list.add(s.nextLine());
        }
        s.close();

        JSentenceDetection detector = new JSentenceDetection("english", "GoogleNews-vectors-negative300.bin.gz");
        assertFalse(detector.detect(list, "Coffee may save your liver from bad binge-drinking habits: 14 percent lower chance of cancer with each cup"));
        assertTrue(detector.detect(list, "Energy Giants Turn to Drones and Sensors in New Embrace of the Digital World"));
        try {
            detector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
