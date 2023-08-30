package com.training.trackMailApi.service;

import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.model.Mailing;

import java.util.List;

public interface HistoryService {

    List<HistoryDto> getAllByMailingId(Long mailingId);

    void saveHistoryForRegisteredMailing(Mailing mailing);

    void saveHistoryForArrivedMailingToPostOffice(Mailing mailing);

    void saveHistoryForDepartureMailingPostOffice(Mailing mailing);

    void saveHistoryForReceivedMailingByRecipient(Mailing mailing);
}
