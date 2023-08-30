package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.converter.HistoryConverter;
import com.training.trackMailApi.dao.HistoryDAO;
import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.model.History;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.service.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private static final String ACTION_REGISTER_MAILING = "Mailing № %s was registered";
    private static final String ACTION_ARRIVE_MAILING_TO_POST_OFFICE = "Mailing № %s was arrived to Post office";
    private static final String ACTION_DEPARTURE_MAILING_FROM_POST_OFFICE = "Mailing № %s left Post office";
    private static final String ACTION_RECEIVE_MAILING_BY_RECIPIENT = "Mailing № %s was received by recipient";

    private final HistoryDAO historyDAO;
    private final HistoryConverter historyConverter;

    @Override
    @Transactional
    public List<HistoryDto> getAllByMailingId(Long mailingId) {
        return historyDAO.getAllByMailingId(mailingId).stream()
                .map(historyConverter::convertToHistoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveHistoryForRegisteredMailing(Mailing mailing) {
        saveHistoryParameters(ACTION_REGISTER_MAILING, mailing, MailStatus.REGISTRATION);
    }

    @Override
    @Transactional
    public void saveHistoryForArrivedMailingToPostOffice(Mailing mailing) {
        saveHistoryParameters(ACTION_ARRIVE_MAILING_TO_POST_OFFICE, mailing, MailStatus.POST_OFFICE_ARRIVAL);
    }

    @Override
    @Transactional
    public void saveHistoryForDepartureMailingPostOffice(Mailing mailing) {
        saveHistoryParameters(ACTION_DEPARTURE_MAILING_FROM_POST_OFFICE, mailing, MailStatus.POST_OFFICE_DEPARTURE);
    }

    @Override
    @Transactional
    public void saveHistoryForReceivedMailingByRecipient(Mailing mailing) {
        saveHistoryParameters(ACTION_RECEIVE_MAILING_BY_RECIPIENT, mailing, MailStatus.ADDRESS_RECEIPT);
    }

    private void saveHistoryParameters(String action, Mailing mailing, MailStatus mailStatus) {
        History history = new History();
        Long mailingId = mailing.getId();

        history.setDate(LocalDateTime.now());
        history.setMailing(mailing);
        history.setMailStatus(mailStatus);
        history.setAction(String.format(action, mailingId));

        historyDAO.save(history);
    }
}
