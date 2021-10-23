package com.adam.project.service;

import com.adam.project.model.Role;
import com.adam.project.model.User;
import com.adam.project.repository.UserRepository;
import com.adam.project.utils.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private FileUtils fileUtils;

    @MockBean
    private Model model;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setEmail("someemail.com");
        boolean isUserCreated = userService.addUser(user, null);
        assertTrue(isUserCreated);
        assertNotNull(user.getActivationCode());
        assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.QUESTIONER)));

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    public void addUserFailTest() throws Exception{
        when(userRepository.findByEmail("someemail.com")).thenReturn(new User());

        User user = new User();
        user.setEmail("someemail.com");
        boolean isUserCreated = userService.addUser(user, null);

        assertFalse(isUserCreated);
        Mockito.verify(userRepository, Mockito.times(0)).save(any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(anyString(), anyString(), anyString());
    }

    @Test
    public void activateUser() {
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("", ""));
        User user = new User();
        user.setActivationCode("bingo");
        when(userRepository
                .findByActivationCode("activate"))
                .thenReturn(user);

        boolean isActivated = userService.activateUser("activate", model, null, null);

        assertTrue(isActivated);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        assertNull(user.getActivationCode());
    }

    @Test
    public void activateUserFailTest(){
        boolean isActivated = userService.activateUser("activate me", model, null, null);

        assertFalse(isActivated);

        Mockito.verify(userRepository, Mockito.times(0)).save(any(User.class));
    }

}