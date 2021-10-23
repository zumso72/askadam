package com.adam.project.controller;

import com.adam.project.model.Question;
import com.adam.project.model.User;
import com.adam.project.model.dto.QuestionForAnsweringDTO;
import com.adam.project.repository.QuestionRepository;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/answer")
@PreAuthorize("hasAuthority('ANSWERER')")
public class AnswererController {

    private final QuestionRepository questionRepository;
    private final UserService userService;

    @Autowired
    public AnswererController(QuestionRepository questionRepository, UserService userService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    @GetMapping
    public String getQuestions(Model model){
        model.addAttribute("questions", questionRepository.getQuestionByAnswerIsNullOrderByTimeDesc());
        return "answer";
    }

    @PostMapping("/{question}")
    public String answer(@PathVariable Question question,
                         @ModelAttribute @Valid QuestionForAnsweringDTO questionDTO,
                         BindingResult bindingResult,
                         Model model){
        if (bindingResult.hasErrors())
            return "questionForAnswerer";
        question.getAuthor().setNewAnswers(true);
        question.setAnswer(questionDTO.getAnswer());
        question.setAnswerTime(new Date());
        questionRepository.save(question);
        return "redirect:/answer";
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("isNewAnswers", userService.get(user.getId()).isNewAnswers());
    }
}
