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



    private HashSet<String> importDictionary(String filename) throws URISyntaxException {
        //final URI resourceUri = URI.create(filename);
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = Paths.get(classLoader.getResource(filename).toURI());

        try {
            //File file = ResourceUtils.getFile(resourceUri);
            //String content = new String(Files.readAllBytes(file.toPath()));

            return Files.readAllLines(file).stream().collect(Collectors.toCollection(HashSet::new));

        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostConstruct
    public void loadDictionaries() throws URISyntaxException {
        polishDictionary.setDictionary(importDictionary(PL_DICTIONARY));
        englishDictionary.setDictionary(importDictionary(EN_DICTIONARY));

    }

    public String detectLanguage(String textToDetect) {
        int polishCounter = 0, englishCounter = 0;

        textToDetect = textToDetect.replaceAll("[\\-+.\\^:,?!'\"()\\[\\];<>]", "").trim().toLowerCase();
        List<String> text = Arrays.asList(textToDetect.split("\\s* \\s*"));
        List<String> enteredText = text.stream().map(this::checkWord).collect(Collectors.toList());

        //enteredText.forEach(e -> e.equals(PL));
        for (String word : enteredText) {
            if (word.equals(PL)) polishCounter++;
            else if (word.equals(EN)) englishCounter++;
        }
        if(polishCounter>englishCounter) return "Polish language detected";
        else if (englishCounter>polishCounter) return "English language detected";
        else return "Not found";
    }


    public String checkWord(String wordToCheck) {
       if(englishDictionary.getDictionary().contains(wordToCheck)) return EN;
       if(polishDictionary.getDictionary().contains(wordToCheck)) return PL;

       return "Not found";
    }





}
