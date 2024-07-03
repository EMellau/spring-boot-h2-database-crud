package com.mellau.spring.jpa.h2.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mellau.spring.jpa.h2.common.enums.Sex;
import com.mellau.spring.jpa.h2.persistence.UserEntity;
import com.mellau.spring.jpa.h2.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        UserEntity user1 = new UserEntity();
        user1.setEmail("user1@example.com");
        user1.setGold(100);
        user1.setSex(Sex.MALE);
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setEmail("user2@example.com");
        user1.setGold(200);
        user2.setSex(Sex.FEMALE);
        userRepository.save(user2);
    }

    @Test
    public void testSearchUser() throws Exception {
        UserSearchRequestDTO searchRequest = new UserSearchRequestDTO();
        searchRequest.setEmail("user1@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'email':'user1@example.com','sex':'MALE'}]"));
    }

    @Test
    public void testGetRanking() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/ranking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].gold").value(100))
                .andExpect(jsonPath("$[1].gold").value(200));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.sex").value("MALE"));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserCreateRequestDTO createRequest = new UserCreateRequestDTO();
        createRequest.setEmail("newuser@example.com");
        createRequest.setSex(Sex.FEMALE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.sex").value("FEMALE"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserUpdateRequestDTO updateRequest = new UserUpdateRequestDTO();
        updateRequest.setGold(500);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.gold").value(500));
    }

    @Test
    public void testDeleteUser() throws Exception {
        //UserEntity user = userRepository.fin("user1@example.com").orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
