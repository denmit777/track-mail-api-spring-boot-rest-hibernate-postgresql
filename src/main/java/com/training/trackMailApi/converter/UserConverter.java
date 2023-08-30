package com.training.trackMailApi.converter;

import com.training.trackMailApi.model.User;
import com.training.trackMailApi.dto.UserRegisterDto;

public interface UserConverter {

    User fromUserRegisterDto(UserRegisterDto userDto);
}
