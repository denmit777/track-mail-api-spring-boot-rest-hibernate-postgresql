package com.training.trackMailApi.dao;

import com.training.trackMailApi.exception.MailNotFoundException;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.enums.MailStatus;
import com.training.trackMailApi.model.enums.MailType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MailingDAOImplTest {

    @Autowired
    private MailingDAO mailingDAO;

    @Test
    public void givenMailing_whenSave_thenReturnSavedMailing() {
        Mailing mailing = createTestMailing(MailType.LETTER);

        mailingDAO.save(mailing);

        assertThat(mailing).isNotNull();
        assertThat(mailing.getId()).isGreaterThan(0);
    }

    @Test
    public void givenMailingList_whenGetAll_thenMailingList() {
        Mailing mailingOne = createTestMailing(MailType.LETTER);
        Mailing mailingTwo = createTestMailing(MailType.POSTCARD);
        Mailing mailingThree = createTestMailing(MailType.PARCEL_POST);

        mailingDAO.save(mailingOne);
        mailingDAO.save(mailingTwo);
        mailingDAO.save(mailingThree);

        List<Mailing> mailings = mailingDAO.getAll();

        assertThat(mailings).isNotNull();
        assertThat(mailings.size()).isEqualTo(3);
    }

    @Test
    public void givenMailing_whenGetById_thenReturnMailing() {
        Mailing mailing = createTestMailing(MailType.LETTER);
        mailingDAO.save(mailing);

        Mailing searchedMailing = mailingDAO.getById(mailing.getId());

        assertThat(searchedMailing).isNotNull();
    }

    @Test(expected = MailNotFoundException.class)
    public void givenMailing_whenFindByIdButDoesNotExist_thenReturnEmptiness() {
        Mailing mailing = createTestMailing(MailType.LETTER);
        mailingDAO.save(mailing);

        Mailing searchedMailing = mailingDAO.getById(mailing.getId() + 1);

        assertThat(searchedMailing).isNull();
    }

    @Test
    public void givenMailing_whenUpdateMailing_thenReturnUpdatedMailing() {
        Mailing mailing = createTestMailing(MailType.LETTER);
        mailingDAO.save(mailing);

        Mailing savedMailing = mailingDAO.getById(mailing.getId());

        savedMailing.setMailStatus(MailStatus.POST_OFFICE_ARRIVAL);

        mailingDAO.update(savedMailing);

        assertThat(savedMailing.getMailStatus()).isEqualTo(MailStatus.POST_OFFICE_ARRIVAL);
    }

    private Mailing createTestMailing(MailType mailType) {
        Mailing mailing = new Mailing();

        mailing.setMailType(mailType);

        return mailing;
    }
}
