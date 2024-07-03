package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import com.mellau.spring.jpa.h2.domain.UserService;
import com.mellau.spring.jpa.h2.domain.model.User;
import com.mellau.spring.jpa.h2.domain.model.UserCreateRequest;
import com.mellau.spring.jpa.h2.domain.model.UserSearchRequest;
import com.mellau.spring.jpa.h2.domain.model.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDTOMapper mapper;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSearchUser() {
        UserSearchRequestDTO requestDTO = new UserSearchRequestDTO().setEmail("test@example.com");
        UserSearchRequest request = UserSearchRequest.builder().email("test@example.com").build();
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserDTO userDTO = new UserDTO().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);

        when(mapper.toUserSearchRequest(requestDTO)).thenReturn(request);
        when(userService.searchUser(request)).thenReturn(Collections.singletonList(user));
        when(mapper.toUserDTOs(Collections.singletonList(user))).thenReturn(Collections.singletonList(userDTO));

        List<UserDTO> result = userController.searchUser(requestDTO);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals(1, result.get(0).getLevel());
        assertEquals(100, result.get(0).getGold());
        assertEquals(Sex.MALE, result.get(0).getSex());
    }

    @Test
    public void testGetRanking() {
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserDTO userDTO = new UserDTO().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);

        when(userService.getRanking()).thenReturn(Collections.singletonList(user));
        when(mapper.toUserDTOs(Collections.singletonList(user))).thenReturn(Collections.singletonList(userDTO));

        List<UserDTO> result = userController.getRanking();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals(1, result.get(0).getLevel());
        assertEquals(100, result.get(0).getGold());
        assertEquals(Sex.MALE, result.get(0).getSex());
    }

    @Test
    public void testGetUserById() {
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserDTO userDTO = new UserDTO().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);

        when(userService.getUserById(1L)).thenReturn(user);
        when(mapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userController.getUserById(1L);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(1, result.getLevel());
        assertEquals(100, result.getGold());
        assertEquals(Sex.MALE, result.getSex());
    }

    @Test
    public void testCreateUser() {
        UserCreateRequestDTO createRequestDTO = new UserCreateRequestDTO().setEmail("newuser@example.com").setSex(Sex.FEMALE);
        UserCreateRequest createRequest = new UserCreateRequest().setEmail("newuser@example.com").setSex(Sex.FEMALE);
        User user = new User().setId(2L).setEmail("newuser@example.com").setLevel(2).setGold(200).setSex(Sex.FEMALE);
        UserDTO userDTO = new UserDTO().setId(2L).setEmail("newuser@example.com").setLevel(2).setGold(200).setSex(Sex.FEMALE);

        when(mapper.toUserCreateRequest(createRequestDTO)).thenReturn(createRequest);
        when(userService.createUser(createRequest)).thenReturn(user);
        when(mapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userController.createUser(createRequestDTO);
        assertEquals(2L, result.getId());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals(2, result.getLevel());
        assertEquals(200, result.getGold());
        assertEquals(Sex.FEMALE, result.getSex());
    }

    @Test
    public void testUpdateUser() {
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO().setGold(500);
        UserUpdateRequest updateRequest = UserUpdateRequest.builder().gold(500).build();
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(500).setSex(Sex.MALE);
        UserDTO userDTO = new UserDTO().setId(1L).setEmail("test@example.com").setLevel(1).setGold(500).setSex(Sex.MALE);

        when(mapper.toUserUpdateRequest(updateRequestDTO)).thenReturn(updateRequest);
        when(userService.updateUser(eq(1L), eq(updateRequest))).thenReturn(user);
        when(mapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userController.updateUser(1L, updateRequestDTO);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(1, result.getLevel());
        assertEquals(500, result.getGold());
        assertEquals(Sex.MALE, result.getSex());
    }

    @Test
    public void testDeleteUser() {
        userController.deleteUser(1L);
        verify(userService).deleteById(1L);
    }
}
