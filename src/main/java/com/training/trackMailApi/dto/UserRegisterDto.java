package com.training.trackMailApi.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegisterDto {

    private static final String FIELD_IS_EMPTY = "Fields shouldn't be empty";
    private static final String WRONG_SIZE_OF_NAME_OR_PASSWORD = "Login or password shouldn't be less than 4 symbols";
    private static final String INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL_OR_INDEX = "Email or index shouldn't be less than 6 symbols";
    private static final String INVALID_EMAIL = "Email should contain symbol @";

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 4, message = WRONG_SIZE_OF_NAME_OR_PASSWORD)
    private String name;

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 4, message = WRONG_SIZE_OF_NAME_OR_PASSWORD)
    private String password;

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 6, message = INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL_OR_INDEX)
    @Email(regexp = "^[^@|\\.].+@.+\\..+[^@|\\.]$", message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = FIELD_IS_EMPTY)
    @Size(min = 6, message = INVALID_NUMBER_OF_SYMBOLS_FOR_EMAIL_OR_INDEX)
    private String index;

    @NotBlank(message = FIELD_IS_EMPTY)
    private String address;
}
