package com.training.trackMailApi.dao.impl;

import com.training.trackMailApi.dao.MailingDAO;
import com.training.trackMailApi.exception.MailNotFoundException;
import com.training.trackMailApi.model.Mailing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class MailingDAOImpl implements MailingDAO {

    private static final String QUERY_SELECT_FROM_MAILING = "from Mailing";
    private static final String MAILING_NOT_FOUND = "Mailing with id %s not found";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(Mailing mailing) {
        entityManager.persist(mailing);
    }

    @Override
    public List<Mailing> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_MAILING, Mailing.class)
                .getResultList();
    }

    @Override
    public Mailing getById(Long id) {
        return getAll().stream()
                .filter(mail -> id.equals(mail.getId()))
                .findAny()
                .orElseThrow(() -> new MailNotFoundException(String.format(MAILING_NOT_FOUND, id)));
    }

    @Override
    public void update(Mailing mailing) {
        entityManager.merge(mailing);
    }
}
