package jsentencedetection;

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

/**
 * Created by pavel1 on 17.10.16.
 */
public class JSentenceDetectionTest {
    @Test
    public void testBasic(){

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
}
