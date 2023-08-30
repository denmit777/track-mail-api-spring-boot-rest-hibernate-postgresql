package com.training.trackMailApi.dao;

import com.training.trackMailApi.model.History;

import java.util.List;

public interface HistoryDAO {

    void save(History history);

    List<History> getAllByMailingId(Long mailingId);
}
