package com.nnk.springboot.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

abstract class SpringbootSubError {
}

@Data
@EqualsAndHashCode(callSuper = false)
class SpringbootValidationError extends SpringbootSubError {

    private String message;

    SpringbootValidationError(String message) {
        this.message = message;
    }
}