package ru.spbstu.jsentencedetection;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class TextPreprocessorTest {
    private final static Logger logger = Logger.getLogger(TextPreprocessorTest.class);

    @Test
    public void testPunctuationPreprocessor() throws IOException {
        String str = "\tIs this test checking punctuation? Test, for, checking punctuation... " +
                "Punctuation & checking - test.";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "Is this test checking punctuation. Test for checking punctuation. " +
                "Punctuation checking test.";

        assertEquals(expecting, res);
    }

    @Test
    public void testLineBreak() throws IOException {
        String str = "This\nis\nline\nbreak\ntest";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);

        String expecting = "This is line break test";
        assertEquals(expecting, res);
    }

    @Test
    public void testWindowsLineBreak() throws IOException {
        String str = "This\n\ris\n\rwindows\n\rline\n\rbreak";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);

        String expecting = "This is windows line break";
        assertEquals(expecting, res);
    }

    @Test
    public void testHyphen() {
        String str = "Word-with-hyphen containing string.";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);

        assertEquals(str, res);
    }

    @Test
    public void testURLIgnore() {
        String str = "This string contains reference to https://www.google.ru/.";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This string contains reference to.";

        assertEquals(expecting, res);
    }

    @Test
    public void testEmailIgnore() {
        String str = "This string contains email abc-abc@gmail.com.";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This string contains email.";

        assertEquals(expecting, res);
    }

    @Test
    public void testRussianText() {
        String str = "Тут русский текст.";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "Тут русский текст.";

        assertEquals(expecting, res);
    }

    @Test
    public void testTextWithDigits() {
        String str = "This text is99 with 2 digits";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text with digits";

        assertEquals(expecting, res);
    }

    @Test
    public void testImageIgnore() {
        String str = "This text contains image. [image: Встроенное изображение 1]";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains image.";

        assertEquals(expecting, res);
    }

    @Test
    public void testTagReferences() {
        String str = "This text contains reference in tags <www.google.com>";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains reference in tags";

        assertEquals(expecting, res);
    }

    @Test
    public void testEmptyString() {
        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess("1");
        assertEquals("", res);
    }

    @Test
    public void testTagSentence() {
        String str = "This text contains sentence in tags. <Sentence in tag>";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains sentence in tags. Sentence in tag";

        assertEquals(expecting, res);
    }

    @Test
    public void testBrackets() {
        String str = "This text contains sentence in brackets, but not image. [Sentence in brackets]";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains sentence in brackets but not image. Sentence in brackets";

        assertEquals(expecting, res);
    }

    @Test
    public void testParentheses() {
        String str = "This text contains sentence in parentheses. (Sentence in parentheses)";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains sentence in parentheses. Sentence in parentheses";

        assertEquals(expecting, res);
    }

    @Test
    public void testStrangeParentheses() {
        String str = "This text contains sentence in parentheses. (Sentence in parentheses)";

        TextPreprocessor preprocessor = new TextPreprocessor();
        String res = preprocessor.preprocess(str);
        String expecting = "This text contains sentence in parentheses. Sentence in parentheses";

        assertEquals(expecting, res);
    }
}
