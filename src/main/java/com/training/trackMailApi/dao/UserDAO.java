package com.training.trackMailApi.dao;

import com.training.trackMailApi.model.User;

import java.util.List;

public interface UserDAO {

    void save(User user);

    User getByLogin(String login);

    User getById(Long id);

    List<User> getAll();

    void update(User user);
}
