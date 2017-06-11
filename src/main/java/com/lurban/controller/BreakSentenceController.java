package com.lurban.controller;

import com.lurban.service.BreakSentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.BreakIterator;
import java.util.List;

/**
 * Created by Łukasz on 2017-05-20.
 */
@Controller
public class BreakSentenceController {

    @Autowired
    private BreakSentenceService breakSentenceService;

    /**
     * Separation given text into single sentences.
     *
     * @param model       Single sentences.
     * @param textToBreak Text that will be separated into sentences.
     * @return home page and single sentences.
     */
    @RequestMapping(value = "break-sentences", method = RequestMethod.POST)
    public String breakSentences(Model model, @RequestParam(value = "text") String textToBreak) {
        List<String> sentences = breakSentenceService.getSentence(breakSentenceService.breakSentence(textToBreak), textToBreak);

        model.addAttribute("sentences", sentences);
        model.addAttribute("prev", textToBreak != null ? textToBreak : "");
        return "index";
    }


}
