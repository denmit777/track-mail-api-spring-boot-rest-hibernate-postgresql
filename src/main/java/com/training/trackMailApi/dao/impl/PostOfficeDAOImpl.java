package com.training.trackMailApi.dao.impl;

import com.training.trackMailApi.dao.PostOfficeDAO;
import com.training.trackMailApi.exception.PostOfficeNotFoundException;
import com.training.trackMailApi.model.PostOffice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class PostOfficeDAOImpl implements PostOfficeDAO {

    private static final String QUERY_SELECT_FROM_POST_OFFICE = "from PostOffice";
    private static final String POST_OFFICE_NOT_FOUND = "Post office with id %s not found";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(PostOffice postOffice) {
        entityManager.persist(postOffice);
    }

    @Override
    public List<PostOffice> getAll() {
        return entityManager.createQuery(QUERY_SELECT_FROM_POST_OFFICE, PostOffice.class)
                .getResultList();
    }

    @Override
    public PostOffice getById(Long id) {
        return getAll().stream()
                .filter(postOffice -> id.equals(postOffice.getId()))
                .findAny()
                .orElseThrow(() -> new PostOfficeNotFoundException(String.format(POST_OFFICE_NOT_FOUND, id)));
    }

    @Override
    public void update(PostOffice postOffice) {
        entityManager.merge(postOffice);
    }
}
