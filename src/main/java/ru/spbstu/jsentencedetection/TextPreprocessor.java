package ru.spbstu.jsentencedetection;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextPreprocessor {
    private static final String PUNCTUATION_SIGN_REGEX = "[?!.;]";
    private static final String PUNCTUATION_SENTENCE_END_REGEX = ".*([?!.;])$";
    private static final int PUNCTUATION_GROUP = 1;

//    sequence of chars containing only letters and hyphens
    private static final String WORD_REGEX = "(\\p{L}+-*)+";
    private static final String SPLIT_PARTS_REGEX = "^((\\p{L}+-*)+)\\p{Punct}*$";
    private static final int WORD_GROUP = 1;

    private static final String IMAGE_MESSAGE_START = "\\[image:";

    private final Pattern endPunctuationPattern = Pattern.compile(PUNCTUATION_SENTENCE_END_REGEX);
    private final Pattern wordPattern = Pattern.compile(SPLIT_PARTS_REGEX);

    public String preprocess(String str) {
        List<String> words = Arrays.asList(str.split("\\s"));

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < words.size(); ++i) {
            String word = words.get(i);

            if (word.matches(IMAGE_MESSAGE_START)) {
                i = ignoreImage(words, i);
                continue;
            }

            word = word.replaceAll("[(){}<>\\[\\]'\\\"]", "");

            res.append(extractWord(word));

            if (res.length() > 0) {
                res.insert(res.length() - 1, extractPunctuation(word));
            }
        }

        if (res.length() > 0) {
            res.deleteCharAt(res.length() - 1);
        }

        return res.toString();
    }

    private int ignoreImage(List<String> words, int i) {
        while (!words.get(i).matches(".*\\]")) {
            ++i;
        }

        return i;
    }

    private String extractWord(String word) {
        Matcher wordMatcher = wordPattern.matcher(word);

        while (wordMatcher.find()) {
            String strPart = wordMatcher.group(WORD_GROUP);

            if (strPart.matches(WORD_REGEX)) {
                return strPart + " ";
            }
        }

        return "";
    }

    private String extractPunctuation(String word) {
        Matcher endPunctuationMatcher = endPunctuationPattern.matcher(word);

        while (endPunctuationMatcher.find()) {
            String strPart = endPunctuationMatcher.group(PUNCTUATION_GROUP);

            if (strPart.matches(PUNCTUATION_SIGN_REGEX)) {
                return ".";
            }
        }

        return "";
    }
}
