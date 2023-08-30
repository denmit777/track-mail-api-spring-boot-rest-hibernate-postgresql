package com.training.trackMailApi.converter.impl;

import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.model.enums.MailType;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class MailingConverterImplTest {

    private MailingConverterImpl mailingConverter;

    @Before
    public void setUp() throws ParseException {
        mailingConverter = new MailingConverterImpl();
    }

    @Test
    public void convertToMailingDto() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");

        Mailing mailing = new Mailing();

        mailing.setMailType(MailType.LETTER);
        mailing.setMailStatus(MailStatus.REGISTRATION);
        mailing.setCreationDate(LocalDateTime.now());
        mailing.setUser(user);

        MailingDto mailingDto = mailingConverter.convertToMailingDto(mailing);

        assertEquals(mailing.getMailType(), mailingDto.getMailType());
        assertEquals(mailing.getMailStatus(), mailingDto.getMailStatus());
        assertEquals(mailing.getCreationDate(), mailingDto.getCreationDate());
        assertEquals(mailing.getUser().getName(), mailingDto.getRecipientName());
        assertEquals(mailing.getUser().getIndex(), mailingDto.getRecipientIndex());
        assertEquals(mailing.getUser().getAddress(), mailingDto.getRecipientAddress());
    }

    @Test
    public void fromMailingDtoTest() {
        MailType mailType = MailType.PACKAGE;

        Mailing mailing = mailingConverter.fromMailingDto(new MailingDto() {{
            setMailType(mailType);
        }});

        assertEquals(mailType, mailing.getMailType());
    }

    private User createTestUser(Long id, String name, String index, String address, String email, String password) {
        User user = new User();

        user.setId(id);
        user.setName(name);
        user.setIndex(index);
        user.setAddress(address);
        user.setEmail(email);
        user.setPassword(password);

        return user;
    }
}
