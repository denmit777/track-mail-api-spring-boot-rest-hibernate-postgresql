package com.training.trackMailApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.trackMailApi.dto.UserLoginDto;
import com.training.trackMailApi.dto.UserRegisterDto;
import com.training.trackMailApi.model.User;
import com.training.trackMailApi.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String[] CLEAN_TABLES_SQL = {
            "delete from users",
    };

    @AfterEach
    public void resetDb() {
        for (String query : CLEAN_TABLES_SQL) {
            jdbcTemplate.execute(query);
        }
    }

    @Test
    @WithMockUser(username = "denis_mogilev@yopmail.com", password = "12345")
    void registerTest_withStatus201andUserReturned() throws Exception {
        UserRegisterDto userDto = new UserRegisterDto();

        userDto.setName("Denis");
        userDto.setPassword("12345");
        userDto.setEmail("denis_mogilev@yopmail.com");
        userDto.setIndex("212008");
        userDto.setAddress("Mogilev, Lenin str. 4-55");

        mockMvc.perform(
                        post("http://localhost:8081")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("denis_mogilev@yopmail.com"))
                .andExpect(jsonPath("$.index").value("212008"))
                .andExpect(jsonPath("$.address").value("Mogilev, Lenin str. 4-55"));
    }

    @Test
    @WithMockUser(username = "denis_mogilev@yopmail.com", password = "12345")
    void registerNegativeTest_idUserAlreadyPresent_withStatus400andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "denis_mogilev@yopmail.com", "12345", "212008", "Mogilev, Lenin str. 4-50");
        UserRegisterDto userDto = new UserRegisterDto();

        userDto.setName("Denis");
        userDto.setPassword("12345");
        userDto.setEmail("denis_mogilev@yopmail.com");
        userDto.setIndex("212008");
        userDto.setAddress("Mogilev, Lenin str. 4-55");

        mockMvc.perform(
                        post("http://localhost:8081")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.info").value(String.format("User with name %s or email %s is already present", userDto.getName(), userDto.getEmail())));
    }

    @Test
    void authenticationUserTest_withStatus200() throws Exception {
        User user = createTestUser("Denis", "denis1_mogilev@yopmail.com", "12345", "212008", "Mogilev, Lenin str. 4-50");

        UserLoginDto userDto = new UserLoginDto();

        userDto.setLogin("denis1_mogilev@yopmail.com");
        userDto.setPassword("12345");

        mockMvc.perform(
                        post("http://localhost:8081/auth")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void authenticationUserNegativeTest_ifUserNotFound_withStatus404andErrorReturned() throws Exception {
        User user = createTestUser("Denis", "denis_mogilev@yopmail.com", "12345", "212008", "Mogilev, Lenin str. 4-50");

        UserLoginDto userDto = new UserLoginDto();

        userDto.setLogin("another_mogilev@yopmail.com");
        userDto.setPassword("Another");

        mockMvc.perform(
                        post("http://localhost:8081/auth")
                                .content(objectMapper.writeValueAsString(userDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.info").value(String.format("User with login %s not found", userDto.getLogin())));
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8081"))
                .andExpect(status().isForbidden());
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
