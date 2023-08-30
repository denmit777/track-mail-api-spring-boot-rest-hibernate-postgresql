package com.training.trackMailApi.security.service;

import com.training.trackMailApi.exception.UserNotFoundException;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.dao.UserDAO;
import com.training.trackMailApi.security.model.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User with login %s not found";

    private final UserDAO userDAO;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        User user = userDAO.getByLogin(login);

        if (user != null) {
            return new CustomUserDetails(user);
        }

        throw new UserNotFoundException(String.format(USER_NOT_FOUND, login));
    }
}

