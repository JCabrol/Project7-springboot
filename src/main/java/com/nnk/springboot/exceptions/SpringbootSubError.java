package com.nnk.springboot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

interface SpringbootSubError {
}

@Data
@EqualsAndHashCode(callSuper = false)
class SpringbootValidationError implements SpringbootSubError {

    private String message;

    SpringbootValidationError(String message) {
        this.message = message;
    }
}