package com.training.trackMailApi.service;

import com.training.trackMailApi.dto.UserLoginDto;
import com.training.trackMailApi.dto.UserRegisterDto;
import com.training.trackMailApi.model.User;

import java.util.Map;

public interface UserService {

    User save(UserRegisterDto userDto);

    Map<Object, Object> authenticateUser(UserLoginDto userDto);

    User getByLoginAndPassword(String login, String password);
}
