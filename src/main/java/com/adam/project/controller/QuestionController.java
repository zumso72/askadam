package com.adam.project.controller;

import com.adam.project.mapstruct.QuestionQuestionForAskingDTOMapperImpl;
import com.adam.project.model.Question;
import com.adam.project.model.Role;
import com.adam.project.model.User;
import com.adam.project.model.dto.QuestionForAnsweringDTO;
import com.adam.project.model.dto.QuestionForAskingDTO;
import com.adam.project.repository.QuestionRepository;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final UserService userService;
    private QuestionRepository questionRepository;
    private QuestionQuestionForAskingDTOMapperImpl mapper;

    @Autowired
    public QuestionController(UserService userService, QuestionRepository questionRepository) {
        this.userService = userService;
        this.questionRepository = questionRepository;
        mapper = new QuestionQuestionForAskingDTOMapperImpl();
    }

    @GetMapping("/ask")
    @PreAuthorize("hasAuthority('QUESTIONER')")
    public String askAdam(@ModelAttribute("questionDTO") QuestionForAskingDTO questionDTO) {
        return "ask";
    }

    @PostMapping("/ask")
    @PreAuthorize("hasAuthority('QUESTIONER')")
    public String createQuestion(@AuthenticationPrincipal User user,
                                 @ModelAttribute("questionDTO") @Valid QuestionForAskingDTO questionDTO,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "ask";
        Question question = mapper.questionForAskingDTOtoQuestion(questionDTO);
        question.setAuthor(user);
        question.setTime(new Date());
        questionRepository.save(question);
        return "redirect:/";
    }

    @GetMapping("/{question}")
    public String question(@PathVariable Question question, Model model){
        model.addAttribute("question", question);
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (object instanceof String)
            return "question";
        else
            user = (User) object;
        if (user.getRoles().contains(Role.ANSWERER)) {
            if (question.getAnswer() == null)
                model.addAttribute("questionDTO", new QuestionForAnsweringDTO());
            return "questionForAnswerer";
        }
        if (question.isAnonymous() && !question.getAuthor().equals(user))
            return "redirect:/";
        return "question";
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("isNewAnswers", userService.get(user.getId()).isNewAnswers());
    }
}
