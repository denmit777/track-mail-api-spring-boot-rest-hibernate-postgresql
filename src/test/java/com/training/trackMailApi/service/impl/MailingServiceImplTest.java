package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.converter.MailingConverter;
import com.training.trackMailApi.dao.MailingDAO;
import com.training.trackMailApi.dao.PostOfficeDAO;
import com.training.trackMailApi.dao.UserDAO;
import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.exception.InvalidMailingStatusException;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.PostOffice;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.model.enums.MailType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MailingServiceImplTest {

    @Mock
    private MailingDAO mailingDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private MailingConverter mailingConverter;

    @Mock
    private PostOfficeDAO postOfficeDAO;

    @Mock
    HistoryServiceImpl historyService;

    @InjectMocks
    private MailingServiceImpl mailingService;

    @Test
    public void registerTest() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);

        MailingDto mailingDto = createTestMailingDto(MailType.LETTER);

        Mailing mailing = new Mailing();

        mailing.setMailType(mailingDto.getMailType());

        when(mailingConverter.fromMailingDto(mailingDto)).thenReturn(mailing);

        willDoNothing().given(historyService).saveHistoryForRegisteredMailing(mailing);

        Mailing result = mailingService.register(mailingDto, user.getEmail());

        Assert.assertNotNull(result);
        Assert.assertEquals(mailing.getMailType(), result.getMailType());

        verify(mailingDAO, times(1)).save(mailingConverter.fromMailingDto(mailingDto));
        verify(userDAO, times(1)).getByLogin(user.getEmail());
    }

    @Test
    public void arriveToPostOfficeTest() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        PostOffice postOffice = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.REGISTRATION, LocalDateTime.now(), user);

        when(postOfficeDAO.getById(postOffice.getId())).thenReturn(postOffice);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        willDoNothing().given(mailingDAO).update(mailing);

        mailingService.arriveToPostOffice(mailing.getId(), postOffice.getId());

        verify(mailingDAO, times(1)).update(mailing);
        verify(postOfficeDAO, times(1)).update(postOffice);
    }

    @Test
    public void arriveToPostOfficeNegativeTestWhenWrongMailStatus() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        PostOffice postOffice = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.POST_OFFICE_ARRIVAL, LocalDateTime.now(), user);

        when(postOfficeDAO.getById(postOffice.getId())).thenReturn(postOffice);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        assertThrows(InvalidMailingStatusException.class,
                () -> mailingService.arriveToPostOffice(mailing.getId(), postOffice.getId()));
        try {
            mailingService.arriveToPostOffice(mailing.getId(), postOffice.getId());
        } catch (InvalidMailingStatusException e) {
            if (e.getMessage().equals("Invalid status of mailing")) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void leavePostOfficeTest() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        PostOffice postOffice = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.POST_OFFICE_ARRIVAL, LocalDateTime.now(), user);

        when(postOfficeDAO.getById(postOffice.getId())).thenReturn(postOffice);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        willDoNothing().given(mailingDAO).update(mailing);

        mailingService.leavePostOffice(mailing.getId(), postOffice.getId());

        verify(mailingDAO, times(1)).update(mailing);
        verify(postOfficeDAO, times(1)).update(postOffice);
    }

    @Test
    public void leavePostOfficeNegativeTestWhenWrongMailStatus() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        PostOffice postOffice = createTestPostOffice(1L, "Mogilev Central Post Office", "212008");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.REGISTRATION, LocalDateTime.now(), user);

        when(postOfficeDAO.getById(postOffice.getId())).thenReturn(postOffice);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        assertThrows(InvalidMailingStatusException.class,
                () -> mailingService.leavePostOffice(mailing.getId(), postOffice.getId()));
        try {
            mailingService.leavePostOffice(mailing.getId(), postOffice.getId());
        } catch (InvalidMailingStatusException e) {
            if (e.getMessage().equals("Invalid status of mailing")) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void receiveByRecipientTest() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.POST_OFFICE_DEPARTURE, LocalDateTime.now(), user);

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        willDoNothing().given(mailingDAO).update(mailing);

        mailingService.receiveByRecipient(mailing.getId(), user.getEmail());

        verify(mailingDAO, times(1)).update(mailing);
        verify(userDAO, times(1)).getByLogin(user.getEmail());
    }

    @Test
    public void receiveByRecipientNegativeTestWhenWrongMailStatus() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.REGISTRATION, LocalDateTime.now(), user);

        when(userDAO.getByLogin(user.getEmail())).thenReturn(user);
        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        assertThrows(InvalidMailingStatusException.class,
                () -> mailingService.receiveByRecipient(mailing.getId(), user.getEmail()));
        try {
            mailingService.receiveByRecipient(mailing.getId(), user.getEmail());
        } catch (InvalidMailingStatusException e) {
            if (e.getMessage().equals("Invalid status of mailing")) {
                return;
            }
        }
        Assert.fail();
    }

    @Test
    public void getAllTest() {
        User userOne = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");
        User userTwo = createTestUser(2L, "Jimmy", "750406", "Singapore, Sembawang Drive 6-412","jimmy_mogilev@yopmail.com", "1111");

        Mailing mailingOne = createTestMailing(1L, MailType.PACKAGE, MailStatus.POST_OFFICE_DEPARTURE, LocalDateTime.now(), userOne);
        Mailing mailingTwo = createTestMailing(2L, MailType.LETTER, MailStatus.POST_OFFICE_ARRIVAL, LocalDateTime.now(), userTwo);
        Mailing mailingThree = createTestMailing(3L, MailType.PARCEL_POST, MailStatus.REGISTRATION, LocalDateTime.now(), userTwo);
        Mailing mailingFour = createTestMailing(4L, MailType.POSTCARD, MailStatus.ADDRESS_RECEIPT, LocalDateTime.now(), userOne);

        List<Mailing> mailings = asList(mailingOne, mailingTwo, mailingThree, mailingFour);

        List<MailingDto> expected = mailings
                .stream()
                .map(mailingConverter::convertToMailingDto)
                .collect(Collectors.toList());

        when(mailingDAO.getAll()).thenReturn(mailings);
        when(mailingConverter.convertToListMailingDto(mailings)).thenReturn(expected);

        List<MailingDto> actual = mailingService.getAll();

        Assert.assertEquals(4, actual.size());
        Assert.assertEquals(4, expected.size());
        Assert.assertEquals(expected, actual);

        verify(mailingDAO, times(1)).getAll();
        verify(mailingConverter, times(1)).convertToListMailingDto(mailings);
    }

    @Test
    public void getByIdTest() {
        User user = createTestUser(1L, "Den", "212008", "Mogilev, Lenin str. 4-50", "den_mogilev@yopmail.com", "1234");

        Mailing mailing = createTestMailing(1L, MailType.PACKAGE, MailStatus.POST_OFFICE_DEPARTURE, LocalDateTime.now(), user);

        when(mailingDAO.getById(mailing.getId())).thenReturn(mailing);

        MailingDto expected = mailingConverter.convertToMailingDto(mailing);

        MailingDto actual = mailingService.getById(mailing.getId());

        Assert.assertEquals(expected, actual);

        verify(mailingDAO, times(1)).getById(mailing.getId());
    }

    private Mailing createTestMailing(Long id, MailType mailType, MailStatus mailStatus, LocalDateTime creationDate, User user) {
        Mailing mailing = new Mailing();

        mailing.setId(id);
        mailing.setMailType(mailType);
        mailing.setMailStatus(mailStatus);
        mailing.setCreationDate(creationDate);
        mailing.setUser(user);

        return mailing;
    }

    private MailingDto createTestMailingDto(MailType mailType) {
        MailingDto mailingDto = new MailingDto();

        mailingDto.setMailType(mailType);

        return mailingDto;
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

    private PostOffice createTestPostOffice(Long id, String name, String index) {
        PostOffice postOffice = new PostOffice();

        postOffice.setId(id);
        postOffice.setName(name);
        postOffice.setIndex(index);

        return postOffice;
    }
}
