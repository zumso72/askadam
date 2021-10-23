package com.adam.project;

import com.adam.project.controller.MainController;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/questions-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/questions-list-after.sql","/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Test
    @Order(1)
    @WithUserDetails(value = "islamadam@yandex.ru")
    public void mainPageTest() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarSupportedContent']/a").string("Ислам"));
    }

    @Test
    @Order(2)
    @WithUserDetails(value = "islamadam@yandex.ru")
    public void questionListTest() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='questionsList']/div").nodeCount(4))
                .andExpect(xpath("//div[@id='questionsList']/div[@data-id='2']").exists());
    }

    @Test
    @Order(3)
    @WithUserDetails(value = "islamadam@yandex.ru")
    public void addQuestion() throws Exception{
        this.mockMvc.perform(post("/questions/ask")
                .param("topic", "Спорт")
                .param("question", "Вопрос")
                .param("isAnonymous", "false")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        this.mockMvc.perform(formLogin().user("adamibnkhavaj@gmail.com").password("1"));

        this.mockMvc.perform(get("/answer"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='questionsList']/div").nodeCount(1))
                .andExpect(xpath("//div[@id='questionsList']/div[@data-id='10']").exists());
    }
}
