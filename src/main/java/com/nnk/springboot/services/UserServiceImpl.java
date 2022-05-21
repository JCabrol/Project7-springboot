package com.nnk.springboot.services;

import com.nnk.springboot.config.SpringSecurityConfig;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.dto.UserDTO;
import com.nnk.springboot.exceptions.ObjectAlreadyExistingException;
import com.nnk.springboot.exceptions.ObjectNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringSecurityConfig springSecurityConfig;

    /**
     * Create a new user
     *
     * @param userDTO a UserDTO object containing information to create a new user
     * @return the user object created
     */
    @Override
    public User createUser(UserDTO userDTO) {
        log.debug("Function createUser in UserService begin.");
        String userName = userDTO.getUsername();
        if (userRepository.existsByUsername(userName))
        {
            throw new ObjectAlreadyExistingException("The username " + userName + " already exists.");
        }
        User user = new User(userDTO.getUsername(), userDTO.getFullname(), userDTO.getRole());
        user.setPassword(springSecurityConfig.encoder().encode(userDTO.getPassword()));
        user = userRepository.save(user);
        log.info("New user with id number " + user.getId() + " has been created.");
        log.debug("Function createUser in UserService ends without exception.");
        return user;
    }

    /**
     * Get all Users
     *
     * @return a list of all the Users existing
     */
    @Override
    public List<UserDTO> getAllUser() {
        log.debug("Function getAllUser in UserService begin.");
        List<UserDTO> allUserDTO = new ArrayList<>();
        List<User> allUser = userRepository.findAll();
        if (!allUser.isEmpty()) {
            allUser.forEach(user -> {
                UserDTO userDTO = transformUserToDTO(user);
                allUserDTO.add(userDTO);
            });
        }
        log.debug("Function getAllUser in UserService ends without exception.");
        return allUserDTO;
    }

    /**
     * Get a user
     *
     * @param id the id of the user object researched
     * @return the user object researched
     */
    private User getUser(Integer id) throws ObjectNotFoundException {
        log.debug("Function getUser in UserService begin.");
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            log.debug("Function getUser in UserService ends without exception.");
            return user;
        } else {
            throw new ObjectNotFoundException("The user with id number " + id + " was not found.");
        }
    }

    /**
     * Get a user
     *
     * @param id the id of the user object researched
     * @return a UserDTO object containing all information to show from the user researched
     */
    @Override
    public UserDTO getUserDTO(Integer id) throws ObjectNotFoundException {
        log.debug("Function getUserDTO in UserService begin.");
        User user = getUser(id);
        UserDTO userDTO = transformUserToDTO(user);
        log.debug("Function getUserDTO in UserService ends without exception.");
        return userDTO;
    }

    /**
     * Update a user
     *
     * @param id      the id of the user to update
     * @param userDTO a userDTO object containing all information to update
     * @return the user object updated
     */
    @Override
    public User updateUser(Integer id, UserDTO userDTO) throws ObjectNotFoundException {
        log.debug("Function updateUser in UserService begin.");
        User user = getUser(id);
        user.setFullname(userDTO.getFullname());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setPassword(springSecurityConfig.encoder().encode(userDTO.getPassword()));
        user = userRepository.save(user);
        log.info("User with id number " + user.getId() + " has been updated.");
        log.debug("Function updateUser in UserService ends without exception.");
        return user;
    }

    /**
     * Delete a user from its id
     *
     * @param id the id of the user object to delete
     */
    @Override
    public void deleteUser(Integer id) throws ObjectNotFoundException {
        log.debug("Function deleteUser in UserService begin.");
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id number " + id + " has been deleted.");
            log.debug("Function deleteUser in UserService ends without exception.");
        } else {
            throw new ObjectNotFoundException("The user with id number " + id + " was not found so it could not have been deleted");
        }
    }

    /**
     * Transform user to UserDTO object
     *
     * @param user the user object to transform to UserDTO object
     * @return a UserDTO object containing all information to show from the user object
     */
    private UserDTO transformUserToDTO(User user) {
        log.trace("Function transformUserToDTO in UserService begin.");
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getFullname(), user.getRole(), user.getId());
        log.trace("Function transformUserToDTO in UserService ends without exception.");
        return userDTO;
    }
}
