package com.yoshidainasaku.output.bbsdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BbsDemoController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
