package com.mellau.spring.jpa.h2.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mellau.spring.jpa.h2.api.UserController;
import com.mellau.spring.jpa.h2.api.UserCreateRequestDTO;
import com.mellau.spring.jpa.h2.api.UserSearchRequestDTO;
import com.mellau.spring.jpa.h2.api.UserUpdateRequestDTO;
import com.mellau.spring.jpa.h2.common.enums.Sex;
import com.mellau.spring.jpa.h2.config.ApplicationConfig;
import com.mellau.spring.jpa.h2.domain.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import({ApplicationConfig.class})
@ComponentScan(basePackages = "com.mellau.spring.jpa.h2")  // Adjust the package name according to your structure
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        // Step 1: Create a user
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("newuser@example.com").setSex(Sex.FEMALE);

        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("newuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value(Sex.FEMALE.toString()));
    }

    @Test
    public void testSearchUser() throws Exception {
        // Create a user to search
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("searchuser@example.com").setSex(Sex.MALE);
        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Search for the user
        UserSearchRequestDTO searchRequestDTO = new UserSearchRequestDTO().setEmail("searchuser@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("searchuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex").value(Sex.MALE.toString()));
    }

    @Test
    public void testGetRanking() throws Exception {
        // Create a user to check in ranking
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("rankuser@example.com").setSex(Sex.FEMALE);
        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Get the user ranking
        mockMvc.perform(MockMvcRequestBuilders.get(ApplicationConfig.USER_URL + "/ranking"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("rankuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sex").value(Sex.FEMALE.toString()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Create a user to update
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("updateuser@example.com").setSex(Sex.MALE);
        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Update the user's gold
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO().setGold(500);

        mockMvc.perform(MockMvcRequestBuilders.put(ApplicationConfig.USER_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("updateuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gold").value(500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value(Sex.MALE.toString()));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Create a user to retrieve by ID
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("getuser@example.com").setSex(Sex.FEMALE);
        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Get the user by ID
        mockMvc.perform(MockMvcRequestBuilders.get(ApplicationConfig.USER_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("getuser@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gold").value(0))  // Default value is 0
                .andExpect(MockMvcResultMatchers.jsonPath("$.sex").value(Sex.FEMALE.toString()));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Create a user to delete
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("deleteuser@example.com").setSex(Sex.MALE);
        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Delete the user
        mockMvc.perform(MockMvcRequestBuilders.delete(ApplicationConfig.USER_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Perform a final search to ensure the user is deleted
        UserSearchRequestDTO searchRequestDTO = new UserSearchRequestDTO().setEmail("deleteuser@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationConfig.USER_URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
}
