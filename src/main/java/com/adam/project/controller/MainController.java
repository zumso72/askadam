package com.adam.project.controller;

import com.adam.project.model.User;
import com.adam.project.repository.QuestionRepository;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    private QuestionRepository questionRepository;
    private final UserService userService;

    @Autowired
    public MainController(QuestionRepository questionRepository, UserService userService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    @GetMapping
    public String main(Model model){
        model.addAttribute("questions", questionRepository.getQuestionsByAnswerIsNotNull());
        return "main";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        Object object;
        if ((object = SecurityContextHolder.getContext().getAuthentication().getPrincipal()) instanceof User) {
            User user = (User) object;
            model.addAttribute("isNewAnswers", userService.get(user.getId()).isNewAnswers());
        }
    }
}
