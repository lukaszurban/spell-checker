package com.lurban.controller;

import com.lurban.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Łukasz on 2017-04-08.
 */
@Controller
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    /**
     * Return view of home page.
     *
     * @param model content of previous text - an empty string initially
     * @return home page.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("prev", "");
        return "index";
    }

    /**
     * Recognize language in given text.
     * Support polish language and english language at the moment.
     *
     * @param model        Detected language.
     * @param textToDetect Text in which the language is to be checked.
     * @return home page and recognized language.
     */
    @RequestMapping(value = "/language", method = RequestMethod.POST)
    public String detectLanguage(Model model, @RequestParam(value = "text") String textToDetect) {

        String result = languageService.detectLanguage(textToDetect);

        model.addAttribute("result", result);
        model.addAttribute("prev", textToDetect != null ? textToDetect : "");
        return "index";
    }


}


