package com.nnk.springboot.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SpringbootError implements Serializable {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    private String message;

    SpringbootError(HttpStatus status) {
        this.status = status;
    }
}
