package com.nnk.springboot.unitTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("homeTests")
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @Tag("HomeControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN " +
                "WHEN the uri \"/\" is called " +
                "THEN the expected view is displayed.")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/");
            // WHEN
            //the uri "/" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("home"))
                    .andExpect(model().hasNoErrors());
        }
    }

    @Nested
    @Tag("HomeControllerTests")
    @DisplayName("adminHome tests:")
    class AdminHomeTest {
        @DisplayName("GIVEN an admin connected" +
                "WHEN the uri \"/admin/home\" is called " +
                "THEN the page is correctly redirected.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void adminHomeTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/admin/home");
            // WHEN
            //the uri "/admin/home" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/bidList/list"))
                    .andExpect(model().hasNoErrors());
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/admin/home\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void adminHomeUserConnectedTest() throws Exception {
            //GIVEN
            //
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/admin/home");
            // WHEN
            //the uri "/admin/home" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }
}
