package com.training.trackMailApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.trackMailApi.dto.HistoryDto;
import com.training.trackMailApi.dto.MailingDto;
import com.training.trackMailApi.dto.PostOfficeDto;
import com.training.trackMailApi.dto.UserRegisterDto;
import com.training.trackMailApi.model.Mailing;
import com.training.trackMailApi.model.PostOffice;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.model.enums.MailType;
import com.training.trackMailApi.service.HistoryService;
import com.training.trackMailApi.service.MailingService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MailingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private PostOfficeService postOfficeService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] CLEAN_TABLES_SQL = {
            "delete from history",
            "delete from mailing",
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
    void registerTest_withStatus201andMailingReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        MailingDto mailingDto = new MailingDto();

        mailingDto.setMailType(MailType.LETTER);

        mockMvc.perform(
                        post("http://localhost:8081/mailings")
                                .content(objectMapper.writeValueAsString(mailingDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.mailType").value("LETTER"))
                .andExpect(jsonPath("$.mailStatus").value("REGISTRATION"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void arriveToPostOfficeTest_withStatus200andStringReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/arriveToPostOffice/{postOfficeId}", mailingId, postOfficeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Mailing arrived to Post office"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void arriveToPostOfficeNegativeTest_ifMailingNotFound_withStatus404andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId() + 1;
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/arriveToPostOffice/{postOfficeId}", mailingId, postOfficeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(String.format("Mailing with id %s not found", mailingId)));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void arriveToPostOfficeNegativeTest_ifPostOfficeNotFound_withStatus404andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId() + 1;

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/arriveToPostOffice/{postOfficeId}", mailingId, postOfficeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(String.format("Post office with id %s not found", postOfficeId)));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void leaveOfficeTest_withStatus200andStringReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mailingService.arriveToPostOffice(mailingId, postOfficeId);

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/leavePostOffice/{postOfficeId}", mailingId, postOfficeId))
                .andExpect(status().isOk())
                .andExpect(content().string("Mailing left Post office"));
    }

    @Test
    @WithMockUser(username = "jan_minsk@yopmail.com", password = "1589")
    void leavePostOfficeNegativeTest_ifMailingDidNotArrivePostOffice_withStatus400andErrorReturned() throws Exception {
        User user = createTestUser("Jan", "jan_minsk@yopmail.com", "1589", "212008", "Minsk, Good str. 7-99");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/leavePostOffice/{postOfficeId}", mailingId, postOfficeId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Invalid status of mailing"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void receiveByRecipientTest_withStatus200andStringReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mailingService.arriveToPostOffice(mailingId, postOfficeId);
        mailingService.leavePostOffice(mailingId, postOfficeId);

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/receiveByRecipient", mailingId))
                .andExpect(status().isOk())
                .andExpect(content().string("Mailing received by recipient"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void receiveByRecipientNegativeTest_ifMailingDidNotLeavePostOffice_withStatus400andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mailingService.arriveToPostOffice(mailingId, postOfficeId);

        mockMvc.perform(
                        put("http://localhost:8081/mailings/{mailingId}/receiveByRecipient", mailingId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value("Invalid status of mailing"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getAllTest_withStatus200andListOfMailingsReturned() throws Exception {
        List<MailingDto> mailings = mailingService.getAll();

        mockMvc.perform(
                        get("http://localhost:8081/mailings"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mailings)));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getByIdTest_ifMailingRegistered_withStatus200andMailingReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");
        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();

        mockMvc.perform(
                        get("http://localhost:8081/mailings/{mailingId}", mailingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.mailType").value("LETTER"))
                .andExpect(jsonPath("$.mailStatus").value("REGISTRATION"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getByIdTest_ifMailingReceivedByRecipient_withStatus200andMailingReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");
        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();
        long postOfficeId = createTestPostOffice("212008", "Mogilev Central Post Office").getId();

        mailingService.arriveToPostOffice(mailingId, postOfficeId);
        mailingService.leavePostOffice(mailingId, postOfficeId);
        mailingService.receiveByRecipient(mailingId, user.getEmail());

        mockMvc.perform(
                        get("http://localhost:8081/mailings/{mailingId}", mailingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.mailType").value("LETTER"))
                .andExpect(jsonPath("$.mailStatus").value("ADDRESS_RECEIPT"));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getByIdNegativeTest_ifMailingNotFoundById_withStatus404andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");
        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId() + 1;

        mockMvc.perform(
                        get("http://localhost:8081/mailings/{mailingId}", mailingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(String.format("Mailing with id %s not found", mailingId)));
    }

    @Test
    @WithMockUser(username = "den1_mogilev@yopmail.com", password = "1234")
    void getAllHistoryByMailingId_withStatus200andListOfHistoryReturned() throws Exception {
        User user = createTestUser("Denis", "den1_mogilev@yopmail.com", "1234", "212008", "Mogilev, Lenin str. 4-50");

        long mailingId = createTestMailing(MailType.LETTER, user.getEmail()).getId();

        List<HistoryDto> history = historyService.getAllByMailingId(mailingId);

        mockMvc.perform(
                        get("http://localhost:8081/mailings/" + mailingId + "/history"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(history)));
    }

    private Mailing createTestMailing(MailType mailType, String login) {
        MailingDto mailingDto = new MailingDto();

        mailingDto.setMailType(mailType);

        return mailingService.register(mailingDto, login);
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
