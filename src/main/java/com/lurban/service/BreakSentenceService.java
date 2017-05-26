package com.lurban.service;

import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Łukasz on 2017-05-20.
 */
@Service
public class BreakSentenceService {

    private List<String> sentenceList = new ArrayList<>();


    public BreakIterator breakSentence(String textToBreak) {
        String paragraph = textToBreak;
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        //int sentences = count(iterator, paragraph);
        return iterator;

    }
//    public static void main(String[] args) {
//        String paragraph =
//                "Line boundary analysis determines where a text " +
//                        "string can be broken when line-wrapping. The " +
//                        "mechanism correctly handles punctuation and " +
//                        "hyphenated words. Actual line breaking needs to " +
//                        "also consider the available line width and is " +
//                        "handled by higher-level software. ";
//
//        BreakIterator iterator =
//                BreakIterator.getSentenceInstance(Locale.US);
//
//
//        int sentences = count(iterator, paragraph);
//        System.out.println("Number of sentences: " + sentences);
//    }

    public List<String> getSentence(BreakIterator bi, String source) {
        int counter = 0;
        bi.setText(source);

        int lastIndex = bi.first();
        while (lastIndex != BreakIterator.DONE) {
            int firstIndex = lastIndex;
            lastIndex = bi.next();

            if (lastIndex != BreakIterator.DONE) {
                String sentence = source.substring(firstIndex, lastIndex);
                sentenceList.add(sentence+('\n'));
                //System.out.println("sentence = " + sentence);
                counter++;
            }
        }

        //return counter;
        return sentenceList;
    }
}
