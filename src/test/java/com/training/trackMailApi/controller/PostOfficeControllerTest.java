package com.training.trackMailApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.dto.UserRegisterDto;
import com.training.trackMailApi.model.PostOffice;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.service.PostOfficeService;
import com.training.trackMailApi.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostOfficeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostOfficeService postOfficeService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] CLEAN_TABLES_SQL = {
            "delete from users",
            "delete from post_office",
    };

    @AfterEach
    public void resetDb() {
        for (String query : CLEAN_TABLES_SQL) {
            jdbcTemplate.execute(query);
        }
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void saveTest_withStatus201andPostOfficeReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        PostOfficeDto postOfficeDto = new PostOfficeDto();

        postOfficeDto.setIndex("212008");
        postOfficeDto.setName("Mogilev Central Post Office");

        mockMvc.perform(
                        post("http://localhost:8081/post-offices")
                                .content(objectMapper.writeValueAsString(postOfficeDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.index").value("212008"))
                .andExpect(jsonPath("$.name").value("Mogilev Central Post Office"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void saveNegativeTest_ifFieldIsEmpty_withStatus400() throws Exception {
        PostOfficeDto postOfficeDto = new PostOfficeDto();

        postOfficeDto.setIndex("");
        postOfficeDto.setName("Mogilev Central Post Office");

        mockMvc.perform(
                        post("http://localhost:8081/post-offices")
                                .content(objectMapper.writeValueAsString(postOfficeDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getAllTest_withStatus200andListOfPostOfficesReturned() throws Exception {
        List<PostOfficeDto> postOffices = postOfficeService.getAll();

        mockMvc.perform(
                        get("http://localhost:8081/post-offices"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postOffices)));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getByIdTest_withStatus200andPostOfficeReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mockMvc.perform(
                        get("http://localhost:8081/post-offices/{id}", postOfficeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value("212008"))
                .andExpect(jsonPath("$.name").value("Mogilev Central Post Office"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getByIdNegativeTest_whenGetNotExistingPostOffice_thenStatus404NotFound() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId() + 1;

        String error = "Post office with id " + postOfficeId + " not found";

        mockMvc.perform(
                        get("http://localhost:8081/post-offices/{id}", postOfficeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(error));
    }

    private PostOffice createTestPostOffice(String index, String name) {
        PostOfficeDto postOfficeDto = new PostOfficeDto();

        postOfficeDto.setIndex(index);
        postOfficeDto.setName(name);

        return postOfficeService.save(postOfficeDto);
    }

    private User createTestUser(String name, String email, String password, String index, String address) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();

        userRegisterDto.setName(name);
        userRegisterDto.setEmail(email);
        userRegisterDto.setPassword(password);
        userRegisterDto.setIndex(index);
        userRegisterDto.setAddress(address);

        return userService.save(userRegisterDto);
    }
}
