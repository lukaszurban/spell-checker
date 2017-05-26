package com.lurban.service;

import org.languagetool.JLanguageTool;
import org.languagetool.language.Polish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Łukasz on 2017-05-17.
 */
@Service
public class SpellCheckerService {

    private List<String> errorList = new ArrayList<>();
    private List<List<String>> suggestionList = new ArrayList<>();

   public List<String> checkSpell(String textToCheck) {

       JLanguageTool langTool = new JLanguageTool(new Polish());
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
          errorList.add(match.getMessage());
           //return ("Suggested correction(s): " +
                 //  match.getSuggestedReplacements());
          suggestionList.add(match.getSuggestedReplacements());
       }
        return errorList;

   }


}
