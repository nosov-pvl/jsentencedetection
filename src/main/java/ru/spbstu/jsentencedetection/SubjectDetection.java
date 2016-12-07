package ru.spbstu.jsentencedetection;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SubjectDetection{

    List<String> subjects;
    JSentenceDetection detector;

    public SubjectDetection(String subjectFile, JSentenceDetection det){

        Scanner s = null;
        try {
            s = new Scanner(new File(getClass().getClassLoader().getResource(subjectFile).getFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        subjects = new ArrayList<String>();
        while (s.hasNextLine()){
            subjects.add(s.nextLine());
        }
        s.close();

        detector = det;
    }

    public Boolean detect(List<String> data) {
       for(String subject : subjects){
           if(detect(data, subject)){
               return true;
           }
       }
       return false;
    }

    public Boolean detect(List<String> data, String value) {
        return detector.detect(data, value);
    }
}
