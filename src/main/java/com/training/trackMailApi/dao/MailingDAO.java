package com.training.trackMailApi.dao;

import com.training.trackMailApi.model.Mailing;

import java.util.List;

public interface MailingDAO {

    void save(Mailing mailing);

    List<Mailing> getAll();

    Mailing getById(Long id);

    void update(Mailing mailing);
}
