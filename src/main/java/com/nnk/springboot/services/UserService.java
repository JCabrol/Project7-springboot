package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.exceptions.ObjectNotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Create a new user
     *
     * @param userDTO a UserDTO object containing information to create a new user
     * @return the user object created
     */
    User createUser(UserDTO userDTO);

    /**
     * Get all Users
     *
     * @return a list of all the Users existing
     */
    List<UserDTO> getAllUser();

    /**
     * Get a user
     *
     * @param id the id of the user object researched
     * @return a UserDTO object containing all information to show from the user researched
     */
    UserDTO getUserDTO(Integer id) throws ObjectNotFoundException;

    /**
     * Update a user
     *
     * @param id the id of the user to update
     * @param userDTO a userDTO object containing all information to update
     * @return the user object updated
     */
    User updateUser(Integer id, UserDTO userDTO) throws ObjectNotFoundException;

    /**
     * Delete a user from its id
     *
     * @param id the id of the user object to delete
     */
    void deleteUser(Integer id) throws ObjectNotFoundException;

}