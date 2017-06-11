package com.lurban.service;

import com.lurban.model.EnglishDictionary;
import com.lurban.model.PolishDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Łukasz on 2017-04-08.
 */

/**
 * Class that provides language detection.
 */
@Service
public class LanguageService {


    private EnglishDictionary englishDictionary;
    private PolishDictionary polishDictionary;

    public static final String PL_DICTIONARY = "dictionaries/polish.txt";
    public static final String EN_DICTIONARY = "dictionaries/english.txt";
    public static final String PL = "PL";
    public static final String EN = "EN";

    @Autowired
    public LanguageService(PolishDictionary polishDictionary, EnglishDictionary englishDictionary) {
        this.polishDictionary = polishDictionary;
        this.englishDictionary = englishDictionary;
    }

    /**
     * Imports words from resource.
     *
     * @param filename Name of the file containing the words to be imported.
     * @return set of words.
     */
    private HashSet<String> importDictionary(String filename) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = Paths.get(classLoader.getResource(filename).toURI());

        try {
            return Files.readAllLines(file).stream().collect(Collectors.toCollection(HashSet::new));

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Sets dictionaries.
     *
     * @throws URISyntaxException
     */
    @PostConstruct
    public void loadDictionaries() throws URISyntaxException {
        polishDictionary.setDictionary(importDictionary(PL_DICTIONARY));
        englishDictionary.setDictionary(importDictionary(EN_DICTIONARY));

    }

    /**
     * Language detection in given text.
     * Support polish language and english language at the moment.
     *
     * @param textToDetect Text that will be checked.
     * @return detected language.
     */
    public String detectLanguage(String textToDetect) {
        int polishCounter = 0, englishCounter = 0;

        textToDetect = textToDetect.replaceAll("[\\-+.\\^:,?!'\"()\\[\\];<>]", "").trim().toLowerCase();
        List<String> text = Arrays.asList(textToDetect.split("\\s* \\s*"));
        List<String> enteredText = text.stream().map(this::checkWord).collect(Collectors.toList());


        for (String word : enteredText) {
            if (word.equals(PL)) polishCounter++;
            else if (word.equals(EN)) englishCounter++;
        }
        if (polishCounter > englishCounter) return "Polish language detected";
        else if (englishCounter > polishCounter) return "English language detected";
        else return "Not found";
    }

    /**
     * Language detection based on dictionaries.
     *
     * @param wordToCheck Word that will be checked.
     * @return detected language of singe word.
     */
    public String checkWord(String wordToCheck) {
        if (englishDictionary.getDictionary().contains(wordToCheck)) return EN;
        if (polishDictionary.getDictionary().contains(wordToCheck)) return PL;

        return "Not found";
    }


}
