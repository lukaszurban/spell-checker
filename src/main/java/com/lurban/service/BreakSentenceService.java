package com.lurban.service;

import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Łukasz on 2017-05-20.
 */

/**
 * Class that provides sentences seperation.
 */
@Service
public class BreakSentenceService {

    /**
     * Seperation of whole text into single sentences.
     *
     * @param textToBreak Text that will be separated into sentences.
     * @return instance for sentence breaks.
     */
    public BreakIterator breakSentence(String textToBreak) {
        String paragraph = textToBreak;
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        return iterator;

    }

    /**
     * @param bi     Instance of BreakIterator class.
     * @param source Text for scanning.
     * @return single sentences.
     */
    public List<String> getSentence(BreakIterator bi, String source) {
        List<String> sentenceList = new ArrayList<>();
        int counter = 0;
        bi.setText(source);

        int lastIndex = bi.first();
        while (lastIndex != BreakIterator.DONE) {
            int firstIndex = lastIndex;
            lastIndex = bi.next();

            if (lastIndex != BreakIterator.DONE) {
                String sentence = source.substring(firstIndex, lastIndex);
                sentenceList.add(sentence + ('\n'));
                counter++;
            }
        }

        return sentenceList;
    }
}
