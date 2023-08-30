package com.training.trackMailApi.converter;

import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.model.Mailing;

import java.util.List;

public interface MailingConverter {

    MailingDto convertToMailingDto(Mailing mailing);

    Mailing fromMailingDto(MailingDto mailingDto);

    List<MailingDto> convertToListMailingDto(List<Mailing> mailings);
}
