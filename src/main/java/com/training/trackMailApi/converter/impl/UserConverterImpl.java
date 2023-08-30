package com.training.trackMailApi.converter.impl;

import com.training.trackMailApi.converter.UserConverter;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.dto.UserRegisterDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserConverterImpl implements UserConverter {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User fromUserRegisterDto(UserRegisterDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setIndex(userDto.getIndex());
        user.setAddress(userDto.getAddress());

        return user;
    }
}
