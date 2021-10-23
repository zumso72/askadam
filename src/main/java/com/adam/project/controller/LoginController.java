package com.adam.project.controller;

import com.adam.project.model.dto.ChangeForgetPasswordDTO;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/forget-password")
    public String forgetPasswordForm(){
        return "forget-password-email";
    }

    @PostMapping("/forget-password")
    public String forgetPassword(@RequestParam("email") String email, Model model){
        model.addAttribute("email", email);
        userService.sendForgetPasswordLink(email);
        return "confPage";
    }

    @GetMapping("/forget-password/{code}")
    public String restorePasswordForm(@PathVariable String code,
                                       @ModelAttribute("passwordDTO") ChangeForgetPasswordDTO changePassword,
                                       Model model){
        changePassword.setCode(code);
        return "restore-password-form";
    }

    @PostMapping("/restore-password")
    public String restorePassword(@ModelAttribute("passwordDTO") ChangeForgetPasswordDTO changePassword,
                                  Model model){
        if (!changePassword.getPassword().equals(changePassword.getPassword2())) {
            model.addAttribute("message", "Пароли не совпадают");
            return "restore-password-form";
        }
        userService.changePasswords(changePassword.getCode(), changePassword);
        model.addAttribute("message", "Пароль успешно изменен");
        return "confResult";
    }
}
