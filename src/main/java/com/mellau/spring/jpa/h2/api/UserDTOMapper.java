package com.mellau.spring.jpa.h2.api;

import com.mellau.spring.jpa.h2.domain.model.User;
import com.mellau.spring.jpa.h2.domain.model.UserCreateRequest;
import com.mellau.spring.jpa.h2.domain.model.UserSearchRequest;
import com.mellau.spring.jpa.h2.domain.model.UserUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDTOMapper {

    public UserSearchRequest toUserSearchRequest(UserSearchRequestDTO dto) {
        // TODO: add prams from SearchRequest
        if (dto == null) {
            return null;
        }

        return UserSearchRequest.builder()
                .email(dto.getEmail())
                .build();
    }

    public UserCreateRequest toUserCreateRequest(UserCreateRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return UserCreateRequest.builder()
                .email(dto.getEmail())
                .sex(dto.getSex())
                .build();
    }

    public UserUpdateRequest toUserUpdateRequest(UserUpdateRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return UserUpdateRequest.builder()
                .gold(dto.getGold())
                .build();
    }

    public List<UserDTO> toUserDTOs(List<User> users) {
        if (users == null) {
            return null;
        }

        return users.stream()
                .map(this::toUserDTO)
                .toList();
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .level(user.getLevel())
                .gold(user.getGold())
                .sex(user.getSex())
                .build();
    }
}