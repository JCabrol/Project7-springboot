package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("loginTests")
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @Tag("LoginControllerTests")
    @DisplayName("login tests:")
    class LoginTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/app/login\" is called " +
                "THEN the expected view is displayed.")
        @Test
        void loginTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/app/login");
            // WHEN
            //the uri "/app/login" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }

    @Nested
    @Tag("LoginControllerTests")
    @DisplayName("getAllUserArticle tests:")
    class GetAllUserArticlesTest {
        @DisplayName("GIVEN an admin connected" +
                "WHEN the uri \"/app/secure/article-details\" is called " +
                "THEN the page is correctly redirected.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void getAllUserArticleTest() throws Exception {
            //GIVEN
            //an admin connected
            List<UserDTO> userDTOList = new ArrayList<>();
            when(userService.getAllUser()).thenReturn(userDTOList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/app/secure/article-details");
            // WHEN
            //the uri "/app/secure/article-details" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the page is correctly redirected
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("userList", userDTOList));
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/app/secure/article-details\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void getAllUserArticleUserConnectedTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/app/secure/article-details");
            // WHEN
            //the uri "/admin/home" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Tag("LoginControllerTests")
    @DisplayName("error tests:")
    class ErrorTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/app/error\" is called " +
                "THEN the expected view is displayed.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void errorTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/app/error");
            // WHEN
            //the uri "/app/error" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(model().hasNoErrors());
        }
    }
}
