package com.yoshidainasaku.output.bbsdemo.controller;

import com.yoshidainasaku.output.bbsdemo.form.SignupForm;
import com.yoshidainasaku.output.bbsdemo.persistence.entity.Content;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.ContentRepository;
import com.yoshidainasaku.output.bbsdemo.persistence.repository.LoginUserRepository;
import com.yoshidainasaku.output.bbsdemo.service.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    private final String MAX_DISPLAY_RESULT = "5";
    private final String DISPLAY_PAGE_SIZE = "5";

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
                         HttpServletRequest request,
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken == false) {
            SecurityContextHolder.clearContext();
        }

        try {
            request.login(signupForm.getUserId(), signupForm.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam HashMap<String, String> params) {
        String currentPage = params.get("page");
        if (currentPage == null) {
            currentPage = "1";
        }
        System.out.println("パラメータの値: " + params);
        System.out.println("現在のページ: " + currentPage);

        HashMap<String, String> paginationInfo = new HashMap<>();  // {現在のページ, 1ページに最大で表示できるコンテンツ量}
        paginationInfo.put("page", currentPage);
        paginationInfo.put("limit", MAX_DISPLAY_RESULT);

        int totalContentsCount = 0;
        List<Content> contentList = null;

        try {
            totalContentsCount = contentRepository.countAllContents();
            contentList = contentRepository.findAll(paginationInfo);
        } catch (Exception e) {
            System.out.println("データ取得に関してエラー: ページング処理に異常あり");
            System.out.println("総データ数: " + totalContentsCount);
            System.out.println("データ: " + contentList);
            System.out.println("DAOに渡す引数paginationInfo: " + paginationInfo);
        }

        // Todo: ページング処理を記述
        int totalPage = totalContentsCount % Integer.parseInt(MAX_DISPLAY_RESULT) > 0 ?
                totalContentsCount / Integer.parseInt(MAX_DISPLAY_RESULT) + 1 :
                totalContentsCount / Integer.parseInt(MAX_DISPLAY_RESULT);
        int startPage = Integer.parseInt(currentPage) % Integer.parseInt(DISPLAY_PAGE_SIZE) == 0 ?
                (Integer.parseInt(currentPage) / Integer.parseInt(DISPLAY_PAGE_SIZE) - 1) * Integer.parseInt(DISPLAY_PAGE_SIZE) + 1 :
                (Integer.parseInt(currentPage) / Integer.parseInt(DISPLAY_PAGE_SIZE)) * Integer.parseInt(DISPLAY_PAGE_SIZE) + 1;
        int endPage = Integer.parseInt(currentPage) % Integer.parseInt(DISPLAY_PAGE_SIZE) == 0 ?
                (Integer.parseInt(currentPage) / Integer.parseInt(DISPLAY_PAGE_SIZE) - 1) * Integer.parseInt(DISPLAY_PAGE_SIZE) + Integer.parseInt(DISPLAY_PAGE_SIZE) :
                (Integer.parseInt(currentPage) / Integer.parseInt(DISPLAY_PAGE_SIZE)) * Integer.parseInt(DISPLAY_PAGE_SIZE) + Integer.parseInt(DISPLAY_PAGE_SIZE);

        // コンテンツ表示の際、コンテンツが取得できなかったらどうする問題を考える（案：データ処理のcatch句で「error画面」を作成してもいいよね
        model.addAttribute("contentList", contentList);
        model.addAttribute("totalContentsCount", totalContentsCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
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
