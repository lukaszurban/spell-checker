package com.lurban.service;

import com.lurban.model.EnglishDictionary;
import com.lurban.model.PolishDictionary;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.BritishEnglish;
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
@Service
public class SpellCheckerService {

    private static final String EMPTY = "No errors found!";
    private static final Integer SIZE = 20;
@Autowired
LanguageService languageService;




//   public List<String> checkSpell(String textToCheck) {
//
//       JLanguageTool langTool = new JLanguageTool(new Polish());
//       List<RuleMatch> matches = null;
//       try {
//           matches = langTool.check(textToCheck);
//       }
//       catch (java.io.IOException e) {
//           throw new RuntimeException(e);
//       }
//
//       for (RuleMatch match : matches) {
//           //return  ("Potential error at characters " +
//           //        match.getFromPos() + "-" + match.getToPos() + ": " +
//          //         match.getMessage());
//          errorList.add(match.getMessage());
//           //return ("Suggested correction(s): " +
//                 //  match.getSuggestedReplacements());
//          suggestionList.add(match.getSuggestedReplacements());
//       }
//        return errorList;
//   }

    public Set<String> checkSpell(String textToCheck) {
        JLanguageTool langTool;

        //List<String> errorList = new ArrayList<>();
        Set<String> errorList = new HashSet<>();
        if(languageService.detectLanguage(textToCheck)== "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        }
        else {
            langTool = new JLanguageTool(new AmericanEnglish());
        }
            //JLanguageTool langTool = new JLanguageTool(new Polish());
        List<RuleMatch> matches = null;

        try {
            matches = langTool.check(textToCheck);
        }
        catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        for (RuleMatch match : matches) {

            //return  ("Potential error at characters " +
            //        match.getFromPos() + "-" + match.getToPos() + ": " +
            //         match.getMessage());
            errorList.add((match.getMessage().replaceAll("<suggestion>","'").replaceAll("</suggestion>","'"))+('\n'));
            //return ("Suggested correction(s): " +
            //  match.getSuggestedReplacements());

            //suggestionList.add(match.getSuggestedReplacements().toString());
        }
        if ((errorList.isEmpty())) {
            Collections.addAll(errorList, EMPTY);
        }
        return errorList;
    }



    public List<String> getSuggestions(String textToCheck) {

        JLanguageTool langTool;

        if(languageService.detectLanguage(textToCheck)== "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        }
        else {
            langTool = new JLanguageTool(new AmericanEnglish());
        }

        List<String> suggestionList = new ArrayList<>();
        List<RuleMatch> matches = null;
        try {
            matches = langTool.check(textToCheck);
        }
        catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }

        for (RuleMatch match : matches) {
            //return  ("Potential error at characters " +
            //        match.getFromPos() + "-" + match.getToPos() + ": " +
            //         match.getMessage());
            //errorList.add(match.getMessage());
            //return ("Suggested correction(s): " +
            //  match.getSuggestedReplacements());
            if(suggestionList.size()<=SIZE)
                suggestionList.add(match.getSuggestedReplacements().stream().limit(3).collect(Collectors.toList()).toString());
            //suggestionList.add(match.getSuggestedReplacements().toString());
        }
        return suggestionList;
    }

//    public List<Integer> getPositions(String textToCheck) {

//        List<Integer> positionList = new ArrayList<>();
//        List<RuleMatch> matches = null;
//        try {
//            matches = langTool.check(textToCheck);
//        }
//        catch (java.io.IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        for (RuleMatch match : matches) {
//            //return  ("Potential error at characters " +
//            //        match.getFromPos() + "-" + match.getToPos() + ": " +
//            //         match.getMessage());
//            //errorList.add(match.getMessage());
//            //return ("Suggested correction(s): " +
//            //  match.getSuggestedReplacements());
//            positionList.add(match.getFromPos());
//            }
//        return positionList;
//
//    }

    public List<String> getCorrectSentence(String textToCheck) {
        JLanguageTool langTool;

        if(languageService.detectLanguage(textToCheck)== "Polish language detected") {
            langTool = new JLanguageTool(new Polish());
        }
        else {
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

            if(!match.getSuggestedReplacements().isEmpty()) {
                correctSentence.replace(match.getFromPos() - offset, match.getToPos() - offset, match.getSuggestedReplacements().get(0));
                offset += (match.getToPos() - match.getFromPos() - match.getSuggestedReplacements().get(0).length());
                correctText.add(correctSentence.toString());
            }
        }
        return correctText;
    }



}
