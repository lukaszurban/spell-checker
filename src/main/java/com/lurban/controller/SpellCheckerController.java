package com.lurban.controller;

import com.lurban.service.SpellCheckerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * Created by Łukasz on 2017-05-19.
 */
@Controller
public class SpellCheckerController {

    @Autowired
    private SpellCheckerService spellCheckerService;

    /**
     * Finds spelling errors in given text.
     * Support polish language and english language at the moment.
     *
     * @param model        Founds spell errors.
     * @param textToDetect Text in which the spell errors will be checked.
     * @return home page and found spell errors.
     */
    @RequestMapping(value = "/check-spell", method = RequestMethod.POST)
    public String checkSpell(Model model, @RequestParam(value = "text") String textToDetect) {

        Set<String> errors = spellCheckerService.checkSpell(textToDetect);
        List<String> suggestions = spellCheckerService.getSuggestions(textToDetect);
        List<String> correctSentence = spellCheckerService.getCorrectSentence(textToDetect);

        model.addAttribute("errors", errors);
        model.addAttribute("suggestions", suggestions);
        model.addAttribute("correctSentence", correctSentence);

        model.addAttribute("prev", textToDetect != null ? textToDetect : "");
        return "index";
    }
}
