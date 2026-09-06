package com.example.demo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/web/memberlogin")
public class MemberLoginController {

    private final UserService userService;

    public MemberLoginController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // 顯示登入頁面
    // =========================
    @GetMapping
    public String showLoginForm(Model model) {

        model.addAttribute("user", new Users());

        return "member/login";
    }

    // =========================
    // 處理登入
    // =========================
    @PostMapping
    public String processLogin(
            @ModelAttribute Users users,
            Model model,
            HttpSession session) {
        try {Integer userId = userService.loginprocess(
                    users.getEmail(),
                    users.getPassword_hash()
            );
            if (userId == 0) {
                model.addAttribute(
                        "errorMessage",
                        "密碼錯誤！或帳號已被刪除"
                );           
                // 保留 Email
                Users loginUser = new Users();
                loginUser.setEmail(users.getEmail());
                model.addAttribute("user", loginUser);
                return "member/login";
            }
         // 登入成功
            session.setAttribute("loginUserId", userId);

            if (users.getEmail().equals("admin@demo.com") & users.getPassword_hash().equals("1234")) {
            		return "redirect:/web/dashboard";     
            }          	
            Integer buyProductId =
                    (Integer) session.getAttribute("buyProductId");

            if (buyProductId != null) {
                session.removeAttribute("buyProductId");
                return "redirect:/web/frontproducts/buy/" + buyProductId;
            }

            return "redirect:/web/member";
        } catch (RuntimeException e) {
            model.addAttribute(
                    "errorMessage",
                    "信箱或密碼錯誤！"
            );

            Users loginUser = new Users();
            loginUser.setEmail(users.getEmail());

            model.addAttribute("user", loginUser);

            return "member/login";
        }
    }

    // =========================
    // 顯示註冊頁面
    // =========================
    @GetMapping("/create")
    public String showCreateForm(Model model) {

        model.addAttribute("user", new Users());
        model.addAttribute("isEdit", false);

        return "member/register";
    }

    // =========================
    // 處理註冊
    // =========================
    @PostMapping("/create")
    public String createUser(@ModelAttribute Users users,
            Model model,
            HttpSession session) {
    		Integer result = userService.checkregister(users.getEmail(), users.getPassword_hash());
    		if (result == 0) {
    		    model.addAttribute(
    		            "errorMessage",
    		            "帳號已存在！請用別的信箱重新註冊"
    		    );
    		    model.addAttribute("user", users);
    		    model.addAttribute("isEdit", false);

    		    return "member/register";
    		}


       	
        Users createdUser = userService.createUser(
                users.getName(),
                users.getEmail(),
                "member",
                users.getPhone(),
                users.getPassword_hash(),
                "active"
        );

        // 註冊完成後直接登入
        session.setAttribute(
                "loginUserId",
                createdUser.getId()
        );

        return "redirect:/web/member";
    }
}