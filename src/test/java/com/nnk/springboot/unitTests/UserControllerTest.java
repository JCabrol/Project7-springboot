package com.nnk.springboot.unitTests;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.exceptions.ObjectAlreadyExistingException;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("userTests")
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("home tests:")
    class HomeTest {
        @DisplayName("GIVEN a list of user returned by the service and an admin connected" +
                "WHEN the uri \"/user/list\" is called " +
                "THEN the expected view is displayed and the model contains expected attribute.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void homeTest() throws Exception {
            //GIVEN
            //a list of user returned by the service and an admin connected
            List<UserDTO> userDTOList = new ArrayList<>();
            when(userService.getAllUser()).thenReturn(userDTOList);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/list");
            // WHEN
            //the uri "/user/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the expected view is displayed and the model contains expected attribute
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/list"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("userList", userDTOList));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).getAllUser();
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/list\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void homeTestNotAuthorized() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/list");
            // WHEN
            //the uri "/user/list" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(0)).getAllUser();
        }
    }

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("add tests:")
    class AddTest {
        @DisplayName("GIVEN an admin connected" +
                "WHEN the uri \"/user/add\" is called " +
                "THEN the status is \"isOk\" and the expected view is displayed.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void addTest() throws Exception {
            //GIVEN
            //an admin connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/add");
            // WHEN
            //the uri "/user/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the status is "isOk" and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(view().name("user/add"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeExists("user"));
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/add\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void addForbiddenTest() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/add");
            // WHEN
            //the uri "/user/add" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("validate tests:")
    class ValidateTest {

        @DisplayName("GIVEN a userDTO with all required information" +
                "WHEN the uri \"/user/validate\" is called " +
                "THEN a user with expected information is created and the page is correctly redirected.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void validateTest() throws Exception {
            //GIVEN
            // a userDTO with all required information
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            String password = "Password1!";
            User user = new User(userName, fullname, role);
            final ArgumentCaptor<UserDTO> arg = ArgumentCaptor.forClass(UserDTO.class);
            when(userService.createUser(any(UserDTO.class))).thenReturn(user);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/validate")
                    .with(csrf())
                    .param("username", userName)
                    .param("fullname", fullname)
                    .param("role", role)
                    .param("password", password);
            // WHEN
            //the uri "/user/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // a user with expected information is created and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("user"))
                    .andExpect(redirectedUrl("/user/list"));
            //the expected methods have been called with expected arguments
            verify(userService).createUser(arg.capture());
            assertEquals(userName, arg.getValue().getUsername());
            assertEquals(fullname, arg.getValue().getFullname());
            assertEquals(role, arg.getValue().getRole());
        }

        @DisplayName("GIVEN a userDTO without required information" +
                "WHEN the uri \"/user/validate\" is called " +
                "THEN there are 4 errors and the page \"add\" is returned.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void validateMissingInformationTest() throws Exception {
            //GIVEN
            // a userDTO without required information
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/validate")
                    .with(csrf());
            // WHEN
            //the uri "/user/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 4 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("user", 4))
                    .andExpect(view().name("user/add"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(0)).createUser(any(UserDTO.class));
        }

        @DisplayName("GIVEN a userDTO with wrong password" +
                "WHEN the uri \"/user/validate\" is called " +
                "THEN there is 1 errors and the page \"add\" is returned.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void validateWrongPasswordTest() throws Exception {
            //GIVEN
            // a userDTO with wrong password
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/validate")
                    .with(csrf())
                    .param("username", "userName")
                    .param("fullname", "fullname")
                    .param("role", "USER")
                    .param("password", "password");
            // WHEN
            //the uri "/user/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is 1 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("user", 1))
                    .andExpect(view().name("user/add"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(0)).createUser(any(UserDTO.class));
        }

        @DisplayName("GIVEN a userName alreadyExisting" +
                "WHEN the uri \"/user/validate\" is called " +
                "THEN there is 1 errors and the page \"add\" is returned.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void validateAlreadyExistingTest() throws Exception {
            //GIVEN
            // a userDTO with all required information
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            String password = "Password1!";
            ObjectAlreadyExistingException objectAlreadyExistingException = new ObjectAlreadyExistingException("error message");
            when(userService.createUser(any(UserDTO.class))).thenThrow(objectAlreadyExistingException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/validate")
                    .with(csrf())
                    .param("username", userName)
                    .param("fullname", fullname)
                    .param("role", role)
                    .param("password", password);
            // WHEN
            //the uri "/user/validate" is called,
            mockMvc.perform(requestBuilder)
                    // there is 1 errors and the page "add" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("user", 1))
                    .andExpect(view().name("user/add"))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).createUser(any(UserDTO.class));
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/validate\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void validateForbiddenTest() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/validate");
            // WHEN
            //the uri "/user/validate" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("showUpdateForm tests:")
    class ShowUpdateFormTest {

        @DisplayName("GIVEN an existing user" +
                "WHEN the uri \"/user/update/{id}\" is called with \"get\" request" +
                "THEN the corresponding userDTO is attached to the model and the expected view is displayed.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void showUpdateFormTest() throws Exception {
            //GIVEN
            // an existing user
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            int id = 1;
            UserDTO userDTO = new UserDTO(userName, fullname, role, id);
            when(userService.getUserDTO(id)).thenReturn(userDTO);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/update/{id}", id);
            // WHEN
            //the uri "/user/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // the corresponding userDTO is attached to the model and the expected view is displayed
                    .andExpect(status().isOk())
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attribute("user", userDTO))
                    .andExpect(view().name("user/update"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).getUserDTO(id);
        }

        @DisplayName("GIVEN a non-existing user" +
                "WHEN the uri \"/user/update/{id}\" is called with \"get\" request" +
                "THEN an error page is displayed with expected error message.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void showUpdateFormNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing user
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            when(userService.getUserDTO(id)).thenThrow(objectNotFoundException);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/update/{id}", id);
            // WHEN
            //the uri "/user/update/{id}" is called with "get" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).getUserDTO(id);
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/update/{id}\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void showUpdateForbiddenTest() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/update/{id}", 1);
            // WHEN
            //the uri "/user/update/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("updateUser tests:")
    class UpdateUserTest {

        @DisplayName("GIVEN an existing user and all required information to update" +
                "WHEN the uri \"/user/update/{id}\" is called with \"post\" request" +
                "THEN there is no error, the expected information is sent and the page is correctly redirected.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void updateUserTest() throws Exception {
            //GIVEN
            // an existing user and all required information to update
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            String password = "Password1!";
            int id = 1;
            User user = new User(id, userName, fullname, role, password);
            final ArgumentCaptor<UserDTO> arg = ArgumentCaptor.forClass(UserDTO.class);
            when(userService.updateUser(eq(id), any(UserDTO.class))).thenReturn(user);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/update/{id}", id)
                    .with(csrf())
                    .param("username", userName)
                    .param("fullname", fullname)
                    .param("role", role)
                    .param("password", password);
            // WHEN
            //the uri "/user/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error, the expected information is sent and the page is correctly redirected
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().attributeDoesNotExist("user"))
                    .andExpect(redirectedUrl("/user/list"));
            //the expected methods have been called with expected arguments
            verify(userService).updateUser(eq(id), arg.capture());
            assertEquals(userName, arg.getValue().getUsername());
            assertEquals(fullname, arg.getValue().getFullname());
            assertEquals(role, arg.getValue().getRole());
        }

        @DisplayName("GIVEN missing information" +
                "WHEN the uri \"/user/update/{id}\" is called with \"post\" request" +
                "THEN there are 2 errors and the page \"update\" is returned.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void updateUserMissingInformationTest() throws Exception {
            //GIVEN
            // missing information
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/update/{id}", id)
                    .with(csrf());
            // WHEN
            //the uri "/user/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there are 2 errors and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("user", 4))
                    .andExpect(view().name("user/update"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(0)).updateUser(anyInt(), any(UserDTO.class));
        }

        @DisplayName("GIVEN a wrong password" +
                "WHEN the uri \"/user/update/{id}\" is called with \"post\" request" +
                "THEN there is 1 error and the page \"update\" is returned.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void updateUserWrongPasswordTest() throws Exception {
            //GIVEN
            // a wrong password
            int id = 1;
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/user/update/{id}", id)
                    .with(csrf())
                    .param("username", "userName")
                    .param("fullname", "fullname")
                    .param("role", "USER")
                    .param("password", "password");
            // WHEN
            //the uri "/user/update/{id}" is called with "post" request,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is 1 error and the page "update" is returned
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("user"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().attributeErrorCount("user", 1))
                    .andExpect(view().name("user/update"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(0)).updateUser(anyInt(), any(UserDTO.class));
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/update/{id}\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void updateForbiddenTest() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/update/{id}", 1);
            // WHEN
            //the uri "/user/update/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @Tag("UserControllerTests")
    @DisplayName("deleteUser tests:")
    class DeleteUserTest {

        @DisplayName("GIVEN an existing user" +
                "WHEN the uri \"/user/delete/{id}\" is called" +
                "THEN there is no error and the page is correctly redirected.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void deleteUserTest() throws Exception {
            //GIVEN
            // an existing user
            int id = 1;
            doNothing().when(userService).deleteUser(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/delete/{id}", id);
            // WHEN
            //the uri "/user/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // there is no error and the page is correctly redirected
                    .andExpect(status().isFound())
                    .andExpect(model().hasNoErrors())
                    .andExpect(redirectedUrl("/user/list"));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).deleteUser(id);
        }

        @DisplayName("GIVEN a non-existing user" +
                "WHEN the uri \"/user/delete/{id}\" is called" +
                "THEN an error page is displayed with expected error message.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "ADMIN")
        @Test
        void deleteUserNonExistingTest() throws Exception {
            //GIVEN
            // a non-existing user
            int id = 1;
            ObjectNotFoundException objectNotFoundException = new ObjectNotFoundException("error message");
            doThrow(objectNotFoundException).when(userService).deleteUser(id);
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/delete/{id}", id);
            // WHEN
            //the uri "/user/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    // an error page is displayed with expected error message
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"))
                    .andExpect(content().string(containsString("404 NOT_FOUND")))
                    .andExpect(content().string(containsString("error message")));
            //the expected methods have been called with expected arguments
            verify(userService, Mockito.times(1)).deleteUser(id);
        }

        @DisplayName("GIVEN a user connected" +
                "WHEN the uri \"/user/delete/{id}\" is called " +
                "THEN the user is not authorized to access the page.")
        @WithMockUser(username = "user", password = "123456Aa*", authorities = "USER")
        @Test
        void deleteForbiddenTest() throws Exception {
            //GIVEN
            //a user connected
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/user/delete/{id}", 1);
            // WHEN
            //the uri "/user/delete/{id}" is called,
            mockMvc.perform(requestBuilder)
                    //THEN
                    //the user is not authorized to access the page
                    .andExpect(status().isForbidden());
        }
    }


}
