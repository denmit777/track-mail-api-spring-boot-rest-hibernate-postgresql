package com.training.trackMailApi.converter.impl;

import com.training.trackMailApi.converter.MailingConverter;
import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.model.Mailing;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MailingConverterImpl implements MailingConverter {

    @Override
    public MailingDto convertToMailingDto(Mailing mailing) {
        MailingDto mailingDto = new MailingDto();

        mailingDto.setId(mailing.getId());
        mailingDto.setRecipientName(mailing.getUser().getName());
        mailingDto.setRecipientAddress(mailing.getUser().getAddress());
        mailingDto.setRecipientIndex(mailing.getUser().getIndex());
        mailingDto.setMailType(mailing.getMailType());
        mailingDto.setMailStatus(mailing.getMailStatus());
        mailingDto.setCreationDate(mailing.getCreationDate());

        return mailingDto;
    }

    @Override
    public Mailing fromMailingDto(MailingDto mailingDto) {
        Mailing mailing = new Mailing();

        mailing.setMailType(mailingDto.getMailType());
        mailing.setCreationDate(LocalDateTime.now());

        return mailing;
    }

    @Override
    public List<MailingDto> convertToListMailingDto(List<Mailing> mailings) {
        return mailings.stream()
                .map(this::convertToMailingDto)
                .collect(Collectors.toList());
    }
}
