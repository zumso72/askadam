package com.adam.project.service;

import com.adam.project.model.Role;
import com.adam.project.model.User;
import com.adam.project.model.dto.ChangePasswordDTO;
import com.adam.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.adam.project.utils.FileUtils;

import java.io.IOException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private Map<User, String> beforeEncodedPasswords;
    private MailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private FileUtils fileUtils;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       MailSender mailSender,
                       @Lazy AuthenticationManager authenticationManager, FileUtils fileUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.authenticationManager = authenticationManager;
        this.fileUtils = fileUtils;
        beforeEncodedPasswords = new HashMap<>();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public Map<User, String> getBeforeEncodedPasswords() {
        return beforeEncodedPasswords;
    }

    public boolean addUser(User user, MultipartFile file) throws IOException {
        if (userRepository.findByEmail(user.getEmail()) != null){
            return false;
        }
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRoles(Collections.singleton(Role.QUESTIONER));
        if (file != null && !file.isEmpty())
            fileUtils.saveFile(file, user);
        String message = String.format(
                "Привет, %s! \n" +
                        "Добро пожаловать на сайт \"Спроси Адама Ельмурзаева\". Пожалуйста, перейдите по ссылке и подтвердите свой аккаунт - \n" +
                        "http://localhost:8080/registration/activate/%s",
                user.getUsername(), user.getActivationCode()
        );
        userRepository.save(user);
        new Thread(() -> mailSender.send(user.getEmail(), "Код активации", message)).start();
        return true;
    }

    public void sendForgetPasswordLink(String email){
        User user = userRepository.findByEmail(email);
        if (user == null) return;
        user.setActivationCode(UUID.randomUUID().toString());
        String message = String.format("Привет, %s! \n" +
                "Добро пожаловать на сайт \"Спроси Адама Ельмурзаева\". Для того чтобы восстановить пароль перейдите по ссылке \n" +
                "http://localhost:8080/forget-password/%s",
                user.getUsername(), user.getActivationCode());
        new Thread(() -> mailSender.send(email, "Восстановление пароля", message)).start();
    }

    public User get(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public boolean activateUser(String code, Model model, String successMessage, String failMessage) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            model.addAttribute("message", failMessage);
            return false;
        }
        user.setActive(true);
        user.setActivationCode(null);
        userRepository.save(user);
        model.addAttribute("message", successMessage);
        model.addAttribute("user", user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), beforeEncodedPasswords.get(user)
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    public void changePasswords(String code, ChangePasswordDTO changePassword) {
        User user = userRepository.findByActivationCode(code);
        user.setPassword(passwordEncoder.encode(changePassword.getPassword()));
        user.setActivationCode(null);
        userRepository.save(user);
    }

    public boolean changePasswords(User user, ChangePasswordDTO changePasswordDTO){
        if (!passwordEncoder.matches(changePasswordDTO.getLastPassword(), user.getPassword()))
            return false;
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        save(user);
        return true;
    }

    public void subscribe(User user, User currentUser) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    public void unsubscribe(User user, User currentUser) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }
}
