package com.yoshidainasaku.output.bbsdemo.controller;

import com.yoshidainasaku.output.bbsdemo.form.SignupForm;
import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.ContentRepository;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.LoginUserRepository;
import com.yoshidainasaku.output.bbsdemo.service.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class BbsDemoController {
    private final ContentRepository contentRepository;
    private final LoginUserRepository loginUserRepository;

    @Autowired
    public BbsDemoController(ContentRepository contentRepository,
                             LoginUserRepository loginUserRepository) {
        this.contentRepository = contentRepository;
        this.loginUserRepository = loginUserRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String newSignup(SignupForm signupForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated SignupForm signupForm,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        if (loginUserRepository.isExist(signupForm.getUserId())) {
            model.addAttribute("signupError", signupForm.getUserId() + " is already exists");
            return "signup";
        }

        try {
            loginUserRepository.registerUser(
                    signupForm.getUserId(),
                    signupForm.getUserName(),
                    signupForm.getPassword(),
                    signupForm.getEmail()
            );
            loginUserRepository.registerUserRole(signupForm.getUserId());
        } catch (DataAccessException error) {
            System.out.println("エラー発生: " + error);
            model.addAttribute("signupError", "ユーザー登録に失敗しました");
            return "signup";
        }

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                       Model model) {
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

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                          Model model) {
        try {
            model.addAttribute("user", loginUserDetails.getLoginUser());

            String userId = loginUserDetails.getUsername();
            Integer count = contentRepository.countSpecificUserContents(userId);
            model.addAttribute("count", count);
            List<Content> contentList = contentRepository.findSpecificUserContents(userId);
            model.addAttribute("contentList", contentList);
        }
        catch (NullPointerException e) {
            model.addAttribute("notLogIn", false);
            return "redirect:/login";
        }

        return "/profile";
    }
}
