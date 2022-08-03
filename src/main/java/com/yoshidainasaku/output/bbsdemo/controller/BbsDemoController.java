package com.yoshidainasaku.output.bbsdemo.controller;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BbsDemoController {
    private final ContentRepository contentRepository;

    @Autowired
    public BbsDemoController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Content> contentList = contentRepository.findAll();
        model.addAttribute("contentList", contentList);
        return "home";
    }
}
