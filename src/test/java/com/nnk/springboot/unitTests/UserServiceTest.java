package com.nnk.springboot.unitTests;

import com.nnk.springboot.config.SpringSecurityConfig;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.exceptions.ObjectAlreadyExistingException;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("userTests")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Nested
    @Tag("UserServiceTests")
    @DisplayName("createUser tests:")
    class CreateUserTest {


        @DisplayName("GIVEN a userDTO containing all required information" +
                "WHEN the function createUser() is called " +
                "THEN a user is created with expected information.")
        @Test
        void createUserTest() {
            //GIVEN
            //a userDTO containing all required information
            String userName = "userName";
            String password = "password";
            String fullname = "fullname";
            String role = "USER";
            SpringSecurityConfig springSecurityConfig = Mockito.mock(SpringSecurityConfig.class);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(userName);
            userDTO.setPassword(password);
            userDTO.setFullname(fullname);
            userDTO.setRole(role);
            final ArgumentCaptor<User> arg = ArgumentCaptor.forClass(User.class);
            when(userRepository.existsByUsername("userName")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(new User());
            when(springSecurityConfig.encoder()).thenReturn(passwordEncoder);
            //WHEN
            //the function createUser() is called
            User result = userService.createUser(userDTO);
            //THEN
            //a user is created with expected information
            assertThat(result).isInstanceOf(User.class);
            //the expected methods have been called with expected arguments
            verify(userRepository).save(arg.capture());
            assertEquals(fullname, arg.getValue().getFullname());
            assertEquals(userName, arg.getValue().getUsername());
            assertEquals(role, arg.getValue().getRole());
            assertEquals(encodedPassword.substring(0, 7), arg.getValue().getPassword().substring(0, 7));
        }

        @DisplayName("GIVEN a user already existing" +
                "WHEN the function createUser() is called " +
                "THEN an ObjectAlreadyExistingException is thrown with the expected error message.")
        @Test
        void createUserAlreadyExistingTest() {
            //GIVEN
            //a user already existing
            String userName = "userName";
            String password = "password";
            String fullname = "fullname";
            String role = "USER";
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(userName);
            userDTO.setPassword(password);
            userDTO.setFullname(fullname);
            userDTO.setRole(role);
            when(userRepository.existsByUsername("userName")).thenReturn(true);
            //WHEN
            //the function createUser() is called
            //THEN
            //an ObjectAlreadyExistingException is thrown with the expected error message
            Exception exception = assertThrows(ObjectAlreadyExistingException.class, () -> userService.createUser(userDTO));
            assertEquals("The username " + userName + " already exists.", exception.getMessage());
        }
    }


    @Nested
    @Tag("UserServiceTests")
    @DisplayName("getAllUser tests:")
    class GetAllUserTest {

        @DisplayName("GIVEN no users returned by userRepository " +
                "WHEN the function getAllUser() is called " +
                "THEN it returns an empty list.")
        @Test
        void getAllUserWhenEmptyTest() {
            //GIVEN
            //no users returned by userRepository
            List<User> allUserTest = new ArrayList<>();
            when(userRepository.findAll()).thenReturn(allUserTest);
            //WHEN
            //the function getAllUser() is called
            List<UserDTO> result = userService.getAllUser();
            //THEN
            //it returns an empty list
            assertThat(result).isEmpty();
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findAll();
        }

        @DisplayName("GIVEN users returned by userRepository " +
                "WHEN the function getAllUser() is called " +
                "THEN it returns the expected list.")
        @Test
        void getAllUserWhenNotEmptyTest() {
            //GIVEN
            //users returned by userRepository
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            String userName2 = "userName2";
            String fullname2 = "fullname2";
            String role2 = "ADMIN";
            User user = new User(userName,fullname,role);
            User user2 = new User(userName2,fullname2,role2);
            List<User> allUserTest = new ArrayList<>();
            allUserTest.add(user);
            allUserTest.add(user2);
            when(userRepository.findAll()).thenReturn(allUserTest);
            //WHEN
            //the function getAllUser() is called
            List<UserDTO> result = userService.getAllUser();
            //THEN
            //it returns the expected list
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getUsername()).isEqualTo(userName);
            assertThat(result.get(0).getFullname()).isEqualTo(fullname);
            assertThat(result.get(0).getRole()).isEqualTo(role);
            assertThat(result.get(1).getUsername()).isEqualTo(userName2);
            assertThat(result.get(1).getFullname()).isEqualTo(fullname2);
            assertThat(result.get(1).getRole()).isEqualTo(role2);
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findAll();
        }
    }


    @Nested
    @Tag("UserServiceTests")
    @DisplayName("getUserDTO tests:")
    class GetUserDTOTest {

        @DisplayName("GIVEN an existing user " +
                "WHEN the function getUserDTO() is called " +
                "THEN it returns a BidLisDTO object with expected information.")
        @Test
        void getUserDTOExistingTest() {
            //GIVEN
            //an existing user
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            User user = new User(userName,fullname,role);
            Integer id = 1;
            user.setId(id);
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            //WHEN
            //the function getUserDTO() is called
            UserDTO result = userService.getUserDTO(id);
            //THEN
            //it returns a BidLisDTO object with expected information
            assertThat(result).isInstanceOf(UserDTO.class);
            assertThat(result.getUsername()).isEqualTo(userName);
            assertThat(result.getFullname()).isEqualTo(fullname);
            assertThat(result.getRole()).isEqualTo(role);
            assertThat(result.getId()).isEqualTo(id);
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findById(id);
        }


        @DisplayName("GIVEN a non-existing user " +
                "WHEN the function getUserDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void getUserDTONonExistingTest() {
            //GIVEN
            //a non-existing user
            Integer id = 1;
            when(userRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getUserDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> userService.getUserDTO(id));
            assertEquals("The user with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findById(id);
        }
    }


    @Nested
    @Tag("UserServiceTests")
    @DisplayName("updateUserDTO tests:")
    class UpdateUserDTOTest {

        @DisplayName("GIVEN an existing user and a UserDTO containing all information" +
                "WHEN the function updateUserDTO() is called " +
                "THEN it returns the User object with information updated and other information unchanged.")
        @Test
        void updateUserDTOExistingTest() {
            //GIVEN
            //an existing user and a UserDTO containing all information
            String userName = "userName";
            String fullname = "fullname";
            String role = "USER";
            String userName2 = "userName2";
            String fullname2 = "fullname2";
            String password2 = "password2";
            String role2 = "ADMIN";
            Integer id = 1;
            User user = new User(userName, fullname, role);
            user.setId(id);
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(userName2);
            userDTO.setFullname(fullname2);
            userDTO.setRole(role2);
            userDTO.setPassword(password2);
            final ArgumentCaptor<User> arg = ArgumentCaptor.forClass(User.class);
            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            //WHEN
            //the function updateUserDTO() is called
            User result = userService.updateUser(id, userDTO);
            //THEN
            // it returns the User object with information updated and other information unchanged
            assertThat(result).isInstanceOf(User.class);
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findById(id);
            verify(userRepository).save(arg.capture());
            assertEquals(userName2, arg.getValue().getUsername());
            assertEquals(fullname2, arg.getValue().getFullname());
            assertEquals(role2, arg.getValue().getRole());
            assertEquals(id, arg.getValue().getId());
        }

        @DisplayName("GIVEN a non-existing user " +
                "WHEN the function updateUserDTO() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void updateUserDTONonExistingTest() {
            //GIVEN
            //a non-existing user
            Integer id = 1;
            UserDTO userDTO = new UserDTO();
            when(userRepository.findById(id)).thenReturn(Optional.empty());
            //WHEN
            //the function getUserDTO() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(id, userDTO));
            assertEquals("The user with id number " + id + " was not found.", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).findById(id);
            verify(userRepository, Mockito.times(0)).save(any(User.class));
        }
    }

    @Nested
    @Tag("UserServiceTests")
    @DisplayName("deleteUser tests:")
    class DeleteUserTest {

        @DisplayName("GIVEN an existing user" +
                "WHEN the function deleteUser() is called " +
                "THEN the expected methods have been called with expected arguments.")
        @Test
        void deleteUserExistingTest() {
            //GIVEN
            //an existing user
            Integer id = 1;
            when(userRepository.existsById(id)).thenReturn(true);
            doNothing().when(userRepository).deleteById(id);
            //WHEN
            //the function deleteUser() is called
            userService.deleteUser(id);
            //THEN
            //the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).existsById(id);
            verify(userRepository, Mockito.times(1)).deleteById(id);
        }

        @DisplayName("GIVEN a non-existing user " +
                "WHEN the function deleteUser() is called " +
                "THEN an ObjectNotFoundException is thrown with the expected error message.")
        @Test
        void deleteUserDTONonExistingTest() {
            //GIVEN
            //a non-existing user
            Integer id = 1;
            when(userRepository.existsById(id)).thenReturn(false);
            //WHEN
            //the function deleteUser() is called
            //THEN
            //an ObjectNotFoundException is thrown with the expected error message
            Exception exception = assertThrows(ObjectNotFoundException.class, () -> userService.deleteUser(id));
            assertEquals("The user with id number " + id + " was not found so it could not have been deleted", exception.getMessage());
            //and the expected methods have been called with expected arguments
            verify(userRepository, Mockito.times(1)).existsById(id);
            verify(userRepository, Mockito.times(0)).deleteById(anyInt());
        }
    }

}
