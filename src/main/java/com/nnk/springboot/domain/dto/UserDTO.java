package com.nnk.springboot.domain.dto;

import com.nnk.springboot.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @ValidPassword
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "FullName is mandatory")
    private String fullname;

    @NotBlank(message = "Role is mandatory")
    private String role;

    public UserDTO(String username, String fullname, String role, Integer id) {
        this.username = username;
        this.fullname = fullname;
        this.role = role;
        this.id = id;
    }
}