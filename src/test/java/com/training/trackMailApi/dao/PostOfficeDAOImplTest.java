package com.training.trackMailApi.dao;

import com.training.trackMailApi.exception.PostOfficeNotFoundException;
import com.training.trackMailApi.model.PostOffice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostOfficeDAOImplTest {

    @Autowired
    private PostOfficeDAO postOfficeDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String CLEAN_TABLE_SQL = "delete from post_office";

    @Test
    public void givenPostOffice_whenSave_thenReturnSavedPostOffice() {
        PostOffice postOffice = createTestPostOffice("Mumbai Central Post Office", "789621");

        postOfficeDAO.save(postOffice);

        assertThat(postOffice).isNotNull();
        assertThat(postOffice.getId()).isGreaterThan(0);
    }

    @Test
    public void givenPostOfficeList_whenGetAll_thenPostOfficeList() {
        jdbcTemplate.execute(CLEAN_TABLE_SQL);

        PostOffice postOfficeOne = createTestPostOffice("Mumbai Central Post Office", "789621");
        PostOffice postOfficeTwo = createTestPostOffice("Melbourne Central Post Office", "589478");
        PostOffice postOfficeThree = createTestPostOffice("Gomel Central Post Office", "232004");

        postOfficeDAO.save(postOfficeOne);
        postOfficeDAO.save(postOfficeTwo);
        postOfficeDAO.save(postOfficeThree);

        List<PostOffice> postOffices = postOfficeDAO.getAll();

        assertThat(postOffices).isNotNull();
        assertThat(postOffices.size()).isEqualTo(3);
    }

    @Test
    public void givenPostOffice_whenGetById_thenReturnPostOffice() {
        PostOffice postOffice = createTestPostOffice("Mumbai Central Post Office", "789621");
        postOfficeDAO.save(postOffice);

        PostOffice searchedPostOffice = postOfficeDAO.getById(postOffice.getId());

        assertThat(searchedPostOffice).isNotNull();
    }

    @Test(expected = PostOfficeNotFoundException.class)
    public void givenPostOffice_whenFindByIdButDoesNotExist_thenReturnEmptiness() {
        PostOffice postOffice = createTestPostOffice("Mumbai Central Post Office", "789621");
        postOfficeDAO.save(postOffice);

        PostOffice searchedPostOffice = postOfficeDAO.getById(postOffice.getId() + 1);

        assertThat(searchedPostOffice).isNull();
    }

    @Test
    public void givenPostOffice_whenUpdatePostOffice_thenReturnUpdatedPostOffice() {
        PostOffice postOffice = createTestPostOffice("Mumbai Central Post Office", "789621");
        postOfficeDAO.save(postOffice);

        PostOffice savedPostOffice = postOfficeDAO.getById(postOffice.getId());

        savedPostOffice.setName("Mogilev Central Post Office");
        savedPostOffice.setIndex("212008");

        postOfficeDAO.update(savedPostOffice);

        assertThat(savedPostOffice.getName()).isEqualTo("Mogilev Central Post Office");
        assertThat(savedPostOffice.getIndex()).isEqualTo("212008");
    }

    private PostOffice createTestPostOffice(String name, String index) {
        PostOffice postOffice = new PostOffice();

        postOffice.setName(name);
        postOffice.setIndex(index);

        return postOffice;
    }
}
