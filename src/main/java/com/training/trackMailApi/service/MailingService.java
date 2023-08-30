package com.training.trackMailApi.service;

import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.model.Mailing;

import java.util.List;

public interface MailingService {

    Mailing register(MailingDto mailingDto, String login);

    void arriveToPostOffice(Long mailingId, Long postOfficeId);

    void leavePostOffice(Long mailingId, Long postOfficeId);

    void receiveByRecipient(Long mailingId, String login);

    List<MailingDto> getAll();

    MailingDto getById(Long mailingId);
}
