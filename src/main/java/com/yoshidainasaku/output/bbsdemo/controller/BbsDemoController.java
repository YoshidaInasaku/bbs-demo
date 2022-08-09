package com.yoshidainasaku.output.bbsdemo.controller;

import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.ContentRepository;
import com.yoshidainasaku.output.bbsdemo.service.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public String home(Authentication authentication, Model model) {
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        String userId = loginUserDetails.getUsername();
        model.addAttribute("userId", userId);

        List<Content> contentList = contentRepository.findAll();
        model.addAttribute("contentList", contentList);
        return "home";
    }
    @PostMapping("/add")
    public String add(@RequestParam("text_content") String textContent,
                      Authentication authentication) {
        LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
        String userId = loginUserDetails.getUsername();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH時mm分");
        String updatedAt = now.format(formatter);

        contentRepository.add(textContent, updatedAt, userId);
        return "redirect:/home";
    }

    @GetMapping("/{user_id}")
    public String profile(@PathVariable("user_id") String userId) {
        return "/profile";
    }
}
