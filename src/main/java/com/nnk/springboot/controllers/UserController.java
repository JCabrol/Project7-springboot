package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.exceptions.ObjectAlreadyExistingException;
import com.nnk.springboot.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private static final String USER_HOME_REDIRECTION = "redirect:/user/list";
    private static final String VIEW_ATTRIBUTE_NAME = "user";

    /**
     * Read - Get all users registered in database
     *
     * @return - A view containing the list of all user
     */
    @ApiOperation(value = "Displays all user registered.")
    @GetMapping("/user/list")
    public String home(Model model) {
        List<UserDTO> userList = userService.getAllUser();
        model.addAttribute("userList", userList);
        return "user/list";
    }

    /**
     * Read - Displays form to add user
     *
     * @return - post "user:add" which is validation and registration for the user to add
     */
    @ApiOperation(value = "Displays a form to add user.")
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute(VIEW_ATTRIBUTE_NAME, userDTO);
        return "user/add";
    }

    /**
     * Create - Add a new user
     *
     * @param user: A userDTO object containing information to create user
     * @return the user form page if there is validation error, the user list page if the user is correctly created
     */
    @ApiOperation(value = "Add a user.")
    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(VIEW_ATTRIBUTE_NAME, user);
            return "user/add";
        }
        try {
            userService.createUser(user);
            return USER_HOME_REDIRECTION;
        } catch (ObjectAlreadyExistingException ex) {
            result.rejectValue("username", "403", ex.getMessage());
            model.addAttribute(VIEW_ATTRIBUTE_NAME, user);
            return "user/add";
        }
    }

    /**
     * Read - Get one user by its id and displays form to update it
     *
     * @param id - An Integer which is the id of the user to update
     * @return a view containing information to show and to update about the selected user
     */
    @ApiOperation(value = "Get a user by its id and displays form to update it.")
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        UserDTO userDTO = userService.getUserDTO(id);
        model.addAttribute(VIEW_ATTRIBUTE_NAME, userDTO);
        return "user/update";
    }

    /**
     * Update - Update an existing user
     *
     * @param id   - An Integer which is the id of the user to update
     * @param user - A UserDTO object containing information to update
     * @return the update form if there is any validation error, the user list page if the user is correctly updated
     */
    @ApiOperation(value = "Update a user by its id.")
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") UserDTO user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            model.addAttribute(VIEW_ATTRIBUTE_NAME, user);
            return "user/update";
        }
        userService.updateUser(id, user);
        return USER_HOME_REDIRECTION;
        }

    /**
     * Delete - Delete a user
     *
     * @param id - An Integer which is the id of the user to delete
     */
    @ApiOperation(value = "Delete a user by its id.")
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.deleteUser(id);
        return USER_HOME_REDIRECTION;
    }
}