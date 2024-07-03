package com.mellau.spring.jpa.h2.domain;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import com.mellau.spring.jpa.h2.domain.model.User;
import com.mellau.spring.jpa.h2.domain.model.UserCreateRequest;
import com.mellau.spring.jpa.h2.domain.model.UserSearchRequest;
import com.mellau.spring.jpa.h2.domain.model.UserUpdateRequest;
import com.mellau.spring.jpa.h2.error.NotFoundException;
import com.mellau.spring.jpa.h2.persistence.UserEntity;
import com.mellau.spring.jpa.h2.persistence.UserEntityMapper;
import com.mellau.spring.jpa.h2.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserEntityMapper mapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void testGetUserById() {
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setLevel(1);
        userEntity.setGold(100);
        userEntity.setSex(Sex.MALE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(mapper.toUser(userEntity)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(1L);
        verify(mapper).toUser(userEntity);
    }

    @Test
    public void testGetUserById_throwsNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The object with id '1' could not be found");
    }

    @Test
    public void testSearchUser() {
        UserSearchRequest searchRequest = UserSearchRequest.builder().email("test@example.com").build();
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setLevel(1);
        userEntity.setGold(100);
        userEntity.setSex(Sex.MALE);

        when(userRepository.findAllByEmail("test@example.com")).thenReturn(Collections.singletonList(userEntity));
        when(mapper.toUsers(Collections.singletonList(userEntity))).thenReturn(Collections.singletonList(user));

        List<User> result = userService.searchUser(searchRequest);

        assertThat(result).containsExactly(user);
        verify(userRepository).findAllByEmail("test@example.com");
        verify(mapper).toUsers(Collections.singletonList(userEntity));
    }

    @Test
    public void testCreateUser() {
        UserCreateRequest createRequest = new UserCreateRequest().setEmail("newuser@example.com").setSex(Sex.FEMALE);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("newuser@example.com");
        userEntity.setLevel(2);
        userEntity.setGold(200);
        userEntity.setSex(Sex.MALE);
        User user = new User().setId(2L).setEmail("newuser@example.com").setLevel(2).setGold(200).setSex(Sex.FEMALE);

        when(mapper.toEntity(createRequest)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.toUser(userEntity)).thenReturn(user);

        User result = userService.createUser(createRequest);

        assertThat(result).isEqualTo(user);
        verify(mapper).toEntity(createRequest);
        verify(userRepository).save(userEntity);
        verify(mapper).toUser(userEntity);
    }

    @Test
    public void testGetRanking() {
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(100).setSex(Sex.MALE);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setLevel(1);
        userEntity.setGold(100);
        userEntity.setSex(Sex.MALE);

        when(userRepository.findAllByOrderByGoldAsc()).thenReturn(Collections.singletonList(userEntity));
        when(mapper.toUsers(Collections.singletonList(userEntity))).thenReturn(Collections.singletonList(user));

        List<User> result = userService.getRanking();

        assertThat(result).containsExactly(user);
        verify(userRepository).findAllByOrderByGoldAsc();
        verify(mapper).toUsers(Collections.singletonList(userEntity));
    }

    @Test
    public void testUpdateUser() {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder().gold(500).build();
        User user = new User().setId(1L).setEmail("test@example.com").setLevel(1).setGold(500).setSex(Sex.MALE);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setLevel(1);
        userEntity.setGold(100);
        userEntity.setSex(Sex.MALE);

        when(userRepository.addGoldByUserId(1L, 500)).thenReturn(Optional.of(userEntity));
        when(mapper.toUser(userEntity)).thenReturn(user);

        User result = userService.updateUser(1L, updateRequest);

        assertThat(result).isEqualTo(user);
        //verify(userRepository).findById(1L);
        verify(userRepository).addGoldByUserId(1L, 500);
        verify(mapper).toUser(userEntity);
    }

    @Test
    public void testUpdateUser_throwsNotFoundException() {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder().gold(500).build();

        assertThatThrownBy(() -> userService.updateUser(1L, updateRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The object with id '1' could not be found");
    }

    @Test
    public void testDeleteById() {
        // No need to mock findById and addGoldByUserId for this method
        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    public void testDeleteById_throwsNotFoundException() {
        doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(1L);

        assertThatThrownBy(() -> userService.deleteById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The object with id '1' could not be found");
    }
}
