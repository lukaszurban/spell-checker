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
/*
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("");
        return "index";
    }*/

    @RequestMapping(value = "/check-spell", method = RequestMethod.POST)
    public String checkSpell(Model model, @RequestParam(value = "text") String textToDetect) {
        //List<String> errors = spellCheckerService.checkSpell(textToDetect);
        Set<String> errors = spellCheckerService.checkSpell(textToDetect);
        List<String> suggestions = spellCheckerService.getSuggestions(textToDetect);
//        List<Integer> positions = spellCheckerService.getPositions(textToDetect);
        List<String> correctSentence = spellCheckerService.getCorrectSentence(textToDetect);
        //SpellCheckerService spell = spellCheckerService.returnValues();

        model.addAttribute("errors",errors);
        model.addAttribute("suggestions",suggestions);
        model.addAttribute("correctSentence", correctSentence);

        //model.addAttribute("positions", positions);
        model.addAttribute("prev", textToDetect != null ? textToDetect : "");
        return "index";
    }
}
