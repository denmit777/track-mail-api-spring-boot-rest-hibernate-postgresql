package com.training.trackMailApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostOfficeDto {
    private static final String INDEX_FIELD_IS_EMPTY = "Index field shouldn't be empty";
    private static final String WRONG_SIZE_OF_INDEX = "Index shouldn't be less than 6 symbols";
    private static final String NAME_FIELD_IS_EMPTY = "Name field shouldn't be empty";

    @NotBlank(message = INDEX_FIELD_IS_EMPTY)
    @Size(min = 6, message = WRONG_SIZE_OF_INDEX)
    private String index;

    @NotBlank(message = NAME_FIELD_IS_EMPTY)
    private String name;

    List<String> recipientAddresses;
}
