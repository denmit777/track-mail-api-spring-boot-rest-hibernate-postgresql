package com.training.trackMailApi.controller;

import com.training.trackMailApi.dto.UserLoginDto;
import com.training.trackMailApi.dto.UserRegisterDto;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.service.UserService;
import com.training.trackMailApi.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping
@Api("User controller")
public class UserController {

    private final UserService userService;
    private final ValidationService validationService;

    @PostMapping("/")
    @ApiOperation(value = "Register a new user")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegisterDto userDto,
                                          BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        User savedUser = userService.save(userDto);

        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        String savedUserLocation = currentUri + "/" + savedUser.getId();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, savedUserLocation)
                .body(savedUser);
    }

    @PostMapping("/auth")
    @ApiOperation(value = "Authenticate and generate JWT token")
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid UserLoginDto userDto,
                                                BindingResult bindingResult) {
        List<String> errorMessage = validationService.generateErrorMessage(bindingResult);

        if (checkErrors(errorMessage)) {
            return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
        }

        Map<Object, Object> response = userService.authenticateUser(userDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean checkErrors(List<String> errorMessage) {
        return !errorMessage.isEmpty();
    }
}
