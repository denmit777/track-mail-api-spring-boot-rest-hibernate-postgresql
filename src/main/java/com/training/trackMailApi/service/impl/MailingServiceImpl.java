package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.converter.MailingConverter;
import com.training.trackMailApi.dao.MailingDAO;
import com.training.trackMailApi.dao.PostOfficeDAO;
import com.training.trackMailApi.dao.UserDAO;
import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.exception.InvalidMailingStatusException;
import com.training.trackMailApi.model.*;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.service.HistoryService;
import com.training.trackMailApi.service.MailingService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MailingServiceImpl implements MailingService {

    private static final Logger LOGGER = LogManager.getLogger(MailingServiceImpl.class.getName());

    private final UserDAO userDAO;
    private final MailingDAO mailingDAO;
    private final PostOfficeDAO postOfficeDAO;
    private final MailingConverter mailingConverter;
    private final HistoryService historyService;

    @Override
    @Transactional
    public Mailing register(MailingDto mailingDto, String login) {
        User recipient = userDAO.getByLogin(login);

        Mailing mailing = mailingConverter.fromMailingDto(mailingDto);

        mailing.setUser(recipient);
        mailing.setMailStatus(MailStatus.REGISTRATION);

        mailingDAO.save(mailing);

        historyService.saveHistoryForRegisteredMailing(mailing);

        LOGGER.info("New mailing : {}", mailing);

        return mailing;
    }

    @Override
    @Transactional
    public void arriveToPostOffice(Long mailingId, Long postOfficeId) {
        Mailing mailing = mailingDAO.getById(mailingId);

        PostOffice postOffice = postOfficeDAO.getById(postOfficeId);

        checkMailStatus(mailing, MailStatus.REGISTRATION);

        postOffice.getMailings().add(mailing);

        postOfficeDAO.update(postOffice);

        mailing.setId(mailingId);
        mailing.setMailStatus(MailStatus.POST_OFFICE_ARRIVAL);

        mailingDAO.update(mailing);

        LOGGER.info("Mailing with id {} has been arrived to Post office with name {}", mailingId, postOffice.getName());

        historyService.saveHistoryForArrivedMailingToPostOffice(mailing);
    }

    @Override
    @Transactional
    public void leavePostOffice(Long mailingId, Long postOfficeId) {
        Mailing mailing = mailingDAO.getById(mailingId);
        PostOffice postOffice = postOfficeDAO.getById(postOfficeId);

        checkMailStatus(mailing, MailStatus.POST_OFFICE_ARRIVAL);

        postOffice.getMailings().remove(mailing);

        postOfficeDAO.update(postOffice);

        mailing.setId(mailingId);
        mailing.setMailStatus(MailStatus.POST_OFFICE_DEPARTURE);

        mailingDAO.update(mailing);

        LOGGER.info("Mailing with id {} has been departure from Post office with name {}", mailingId, postOffice.getName());

        historyService.saveHistoryForDepartureMailingPostOffice(mailing);
    }

    @Override
    @Transactional
    public void receiveByRecipient(Long mailingId, String login) {
        Mailing mailing = mailingDAO.getById(mailingId);
        User recipient = userDAO.getByLogin(login);

        checkMailStatus(mailing, MailStatus.POST_OFFICE_DEPARTURE);

        recipient.getMailings().add(mailing);

        userDAO.update(recipient);

        mailing.setId(mailingId);
        mailing.setMailStatus(MailStatus.ADDRESS_RECEIPT);

        mailingDAO.update(mailing);

        LOGGER.info("Mailing with id {} has been received by {}", mailingId, recipient.getName());

        historyService.saveHistoryForReceivedMailingByRecipient(mailing);
    }

    @Override
    @Transactional
    public List<MailingDto> getAll() {
        return mailingConverter.convertToListMailingDto(mailingDAO.getAll());
    }

    @Override
    @Transactional
    public MailingDto getById(Long mailingId) {
        return mailingConverter.convertToMailingDto(mailingDAO.getById(mailingId));
    }

    private void checkMailStatus(Mailing mailing, MailStatus status) {
        if (mailing.getMailStatus() != status) {
            throw new InvalidMailingStatusException("Invalid status of mailing");
        }
    }
}
