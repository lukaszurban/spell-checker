package com.lurban.service;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.Polish;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Łukasz on 2017-05-17.
 */

/**
 * Class that provides spell checking.
 */
@Service
public class SpellCheckerService {

    private static final String EMPTY = "No errors found!";
    private static final Integer SIZE = 20;
    @Autowired
    LanguageService languageService;

    /**
     * Detection of spelling errors in a given text depending on the language.
     * Support polish language and english language at the moment.
     *
     * @param textToCheck Text in which the spelling will be checked.
     * @return set of detected spelling errors.
     */
    public Set<String> checkSpell(String textToCheck) {
        JLanguageTool langTool;

        Set<String> errorList = new HashSet<>();
        if (languageService.detectLanguage(textToCheck) == "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        } else {
            langTool = new JLanguageTool(new AmericanEnglish());
        }
        List<RuleMatch> matches = null;

        try {
            matches = langTool.check(textToCheck);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        for (RuleMatch match : matches) {

            errorList.add((match.getMessage().replaceAll("<suggestion>", "'").replaceAll("</suggestion>", "'")) + ('\n'));
        }
        if ((errorList.isEmpty())) {
            Collections.addAll(errorList, EMPTY);
        }
        return errorList;
    }

    /**
     * Getting suggestions of correct words.
     * Support polish language and english language at the moment.
     *
     * @param textToCheck Text for which suggestions for correct words will be returned.
     * @return suggestions of correct words.
     */
    public List<String> getSuggestions(String textToCheck) {

        JLanguageTool langTool;

        if (languageService.detectLanguage(textToCheck) == "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        } else {
            langTool = new JLanguageTool(new AmericanEnglish());
        }

        List<String> suggestionList = new ArrayList<>();
        List<RuleMatch> matches = null;
        try {
            matches = langTool.check(textToCheck);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        for (RuleMatch match : matches) {
            if (suggestionList.size() <= SIZE)
                suggestionList.add(match.getSuggestedReplacements().stream().limit(3).collect(Collectors.toList()).toString().replaceAll("\\[|\\]",""));
        }
        return suggestionList;
    }


    /**
     * Getting corrected sentences.
     * Support polish language and english language at the moment.
     *
     * @param textToCheck Text for which suggestions for corrected sentences will be returned.
     * @return corrected sentences.
     */
    public String getCorrectSentence(String textToCheck) {
        JLanguageTool langTool;

        if (languageService.detectLanguage(textToCheck) == "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        } else {
            langTool = new JLanguageTool(new AmericanEnglish());
        }

        List<String> correctText = new ArrayList<>();
        StringBuilder correctSentence = new StringBuilder(textToCheck);
        List<RuleMatch> matches = null;
        try {
            matches = langTool.check(textToCheck);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int offset = 0;
        for (RuleMatch match : matches) {

            if (!match.getSuggestedReplacements().isEmpty()) {
                correctSentence.replace(match.getFromPos() - offset, match.getToPos() - offset, match.getSuggestedReplacements().get(0));
                offset += (match.getToPos() - match.getFromPos() - match.getSuggestedReplacements().get(0).length());
                correctText.add(correctSentence.toString());
            }
        }
        return correctText.get(correctText.size()-1);

    }


}
