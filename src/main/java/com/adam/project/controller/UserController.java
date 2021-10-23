package com.adam.project.controller;

import com.adam.project.mapstruct.ProfileDTOUserMapper;
import com.adam.project.mapstruct.ProfileDTOUserMapperImpl;
import com.adam.project.model.User;
import com.adam.project.model.dto.ChangePasswordDTO;
import com.adam.project.model.dto.ProfileDTO;
import com.adam.project.repository.QuestionRepository;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final ProfileDTOUserMapper profileDTOUserMapper;
    private final UserService userService;
    private final QuestionRepository questionRepository;

    @Autowired
    public UserController(UserService userService, QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.profileDTOUserMapper = new ProfileDTOUserMapperImpl();
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal User principal, Model model){
        User user = userService.get(principal.getId());
        ProfileDTO profileDTO = profileDTOUserMapper.userToProfileDTO(user);
        model.addAttribute("profileDTO", profileDTO);
        return "profile";
    }

    @PostMapping("/change")
    public String changeProfile(@ModelAttribute("profileDTO") ProfileDTO profileDTO, @AuthenticationPrincipal User user){
        user.setFirstName(profileDTO.getFirstName());
        user.setLastName(profileDTO.getLastName());
        userService.save(user);
        return "redirect:/users/profile";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordForm(@ModelAttribute ChangePasswordDTO changePasswordDTO){
        return "change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@ModelAttribute ChangePasswordDTO changePasswordDTO, @AuthenticationPrincipal User user,
                                 Model model){
        String message;
        if (!changePasswordDTO.getPassword().equals(changePasswordDTO.getPassword2())) {
            message = "Пароли не совпадают";
            model.addAttribute("message", message);
            return "change-password";
        }
        else if (!userService.changePasswords(user, changePasswordDTO)){
            message = "Неверный пароль";
            model.addAttribute("message", message);
            return "change-password";
        }
        message = "Пароль успешно изменен";
        model.addAttribute("message", message);
        return "confResult";
    }

    @GetMapping("/{userId}")
    public String getQuestionsByUser(@PathVariable Long userId, Model model){
        User user = userService.get(userId);
        model.addAttribute("user", user);
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser;
        if (object instanceof String) {
            model.addAttribute("questions", questionRepository.getQuestionsByAuthorWhereIsAnonymousFalse(userId));
            model.addAttribute("title", "Вопросы");
            return "userQuestions";
        }
        currentUser = (User) object;
        boolean isCurrentUser = currentUser.equals(user);
        if (!isCurrentUser){
            model.addAttribute("questions", questionRepository.getQuestionsByAuthorWhereIsAnonymousFalse(userId));
            model.addAttribute("title", "Вопросы");
        }
        else {
            model.addAttribute("questions", questionRepository.getQuestionsByAuthor(userId));
            model.addAttribute("title", "Мои вопросы");
            currentUser.setNewAnswers(false);
            userService.save(currentUser);
            model.addAttribute("isNewAnswers", currentUser.isNewAnswers());
        }
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        return "userQuestions";
    }

    @GetMapping("/subscribe/{user}")
    public String subscribe(@PathVariable User user, @AuthenticationPrincipal User currentUser){
        userService.subscribe(user, currentUser);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/unsubscribe/{user}")
    public String unsubscribe(@PathVariable User user, @AuthenticationPrincipal User currentUser){
        userService.unsubscribe(user, currentUser);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/subscribers/{user}")
    public String subscribers(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        return "subscribers";
    }

    @GetMapping("/subscriptions/{user}")
    public String subscriptions(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        return "subscriptions";
    }

    @ModelAttribute
    public void addAttributes(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("isNewAnswers", userService.get(user.getId()).isNewAnswers());
    }
}
