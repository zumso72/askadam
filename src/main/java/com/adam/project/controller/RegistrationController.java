package com.adam.project.controller;

import com.adam.project.mapstruct.UserUserDTOMapperImpl;
import com.adam.project.model.User;
import com.adam.project.model.dto.CaptchaResponseDTO;
import com.adam.project.model.dto.UserDTO;
import com.adam.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final UserUserDTOMapperImpl mapper;
    private RestTemplate restTemplate;
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        mapper = new UserUserDTOMapperImpl();
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String registration(@ModelAttribute("userDTO") UserDTO userDTO){
        return "registration";
    }

    @PostMapping
    public String addUser(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult, Model model,
                          @RequestParam MultipartFile file, @RequestParam("g-recaptcha-response") String captchaResponse) throws IOException {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDTO captchaResponseDto = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDTO.class);
        if (bindingResult.hasErrors()){
            return "registration";
        }
        if (!captchaResponseDto.isSuccess()){
            String message = "Каптча не прошла проверку";
            model.addAttribute("message", message);
            return "registration";
        }
        if (!userDTO.getPassword().equals(userDTO.getPassword2())){
            bindingResult.rejectValue("password2", "userDTO.password2", "Пароли не совпадают");
            return "registration";
        }
        User user = mapper.userDTOToUser(userDTO);
        userService.getBeforeEncodedPasswords().put(user, user.getPassword());
        if (!userService.addUser(user, file)){
            model.addAttribute("message", "Такой email уже занят");
            return "registration";
        }
        model.addAttribute("user", user);
        return "confPage";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model){
        userService.activateUser(code, model, "Вы успешно подтвердили свой аккаунт",
                "Не удалось подтвердить аккаунт");
        model.addAttribute("isNewAnswers", false);
        return "confResult";
    }

}
