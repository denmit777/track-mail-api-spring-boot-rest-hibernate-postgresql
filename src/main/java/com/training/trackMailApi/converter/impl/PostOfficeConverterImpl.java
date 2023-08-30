package com.training.trackMailApi.converter.impl;

import com.training.trackMailApi.converter.PostOfficeConverter;
import com.training.trackMailApi.dao.MailingDAO;
import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.PostOffice;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostOfficeConverterImpl implements PostOfficeConverter {

    private final MailingDAO mailingDAO;

    @Override
    public PostOfficeDto convertToPostOfficeDto(PostOffice postOffice) {
        PostOfficeDto postOfficeDto = new PostOfficeDto();

        postOfficeDto.setIndex(postOffice.getIndex());
        postOfficeDto.setName(postOffice.getName());
        postOfficeDto.setRecipientAddresses(getRecipientAddressesFromMails(postOffice.getMailings()));

        return postOfficeDto;
    }

    @Override
    public PostOffice fromPostOfficeDto(PostOfficeDto postOfficeDto) {
        PostOffice postOffice = new PostOffice();

        postOffice.setIndex(postOfficeDto.getIndex());
        postOffice.setName(postOfficeDto.getName());

        return postOffice;
    }

    @Override
    public List<PostOfficeDto> convertToListPostOfficeDto(List<PostOffice> postOffices) {
        return postOffices.stream()
                .map(this::convertToPostOfficeDto)
                .collect(Collectors.toList());
    }

    private List<String> getRecipientAddressesFromMails(List<Mailing> mailings) {
        return mailings.stream()
                .map(mail -> mail.getUser().getAddress())
                .collect(Collectors.toList());
    }
}
