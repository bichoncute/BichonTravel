package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/user")
public class UserWebController {

    private final UserService userService;

    public UserWebController(UserService userService) {
        this.userService = userService;
    }

    // 顯示使用者列表
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userCount", userService.getUserCount());
        return "user/list";
    }

    // 顯示使用者詳情
    @GetMapping("/{id}")
    public String getUserDetail(@PathVariable Integer id, Model model) {
        return userService.getUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/detail";
                })
                .orElse("redirect:/web/user");
    }

    // 顯示建立表單
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new Users());
        model.addAttribute("isEdit", false);
        return "user/form";
    }

    // 處理建立表單
    @PostMapping("/create")
    public String createUser(
            @ModelAttribute Users users,
            RedirectAttributes redirectAttributes) {

        Users createdUser = userService.createUser(
                users.getName(),
                users.getEmail(),
                users.getRole(),
                users.getPhone(),
                users.getPassword_hash(),
                users.getAccount_status()
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "使用者建立成功！"
        );

        return "redirect:/web/user/" + createdUser.getId();
    }

    // 顯示編輯表單
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Integer id,
            Model model) {

        return userService.getUserById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("isEdit", true);
                    return "user/form";
                })
                .orElse("redirect:/web/user");
    }

    // 處理編輯表單
    @PostMapping("/{id}/edit")
    public String updateUser(
            @PathVariable Integer id,
            @ModelAttribute Users users,
            RedirectAttributes redirectAttributes) {

        userService.updateUser(
                id,
                users.getName(),
                users.getRole(),
                users.getPhone(),
                users.getPassword_hash(),
                users.getAccount_status()
        );
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "使用者更新成功！"
        );
        return "redirect:/web/user/" + id; }

    // 刪除使用者
    @PostMapping("/{id}/delete")
    public String deleteUser(
            @PathVariable Integer id,
            @ModelAttribute Users users,
            RedirectAttributes redirectAttributes) {

        if (userService.deleteUser(id)) {
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "使用者刪除成功！"
            );
        }

        return "redirect:/web/user";
    }
}
