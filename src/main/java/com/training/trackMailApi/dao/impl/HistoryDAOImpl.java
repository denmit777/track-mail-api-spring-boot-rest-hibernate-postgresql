package com.training.trackMailApi.dao.impl;

import com.training.trackMailApi.dao.HistoryDAO;
import com.training.trackMailApi.model.History;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@AllArgsConstructor
public class HistoryDAOImpl implements HistoryDAO {

    private static final String QUERY_SELECT_FROM_HISTORY_BY_MAILING_ID_ORDERED_BY_DATE = "from History h where h.mailing.id =:id order by date DESC";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void save(History history) {
        entityManager.persist(history);
    }

    @Override
    public List<History> getAllByMailingId(Long id) {
        return entityManager.createQuery(QUERY_SELECT_FROM_HISTORY_BY_MAILING_ID_ORDERED_BY_DATE, History.class)
                .setParameter("id", id)
                .getResultList();
    }
}
