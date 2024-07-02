package com.mellau.spring.jpa.h2.persistence;

import com.mellau.spring.jpa.h2.domain.model.User;
import com.mellau.spring.jpa.h2.domain.model.UserCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityMapper {

    public List<User> toUsers(List<UserEntity> entities) {
        if (entities == null) {
            return null;
        }

        return entities.stream()
                .map(this::toUser)
                .toList();
    }

    public User toUser(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .level(entity.getLevel())
                .gold(entity.getGold())
                .sex(entity.getSex())
                .build();
    }

    public UserEntity toEntity(UserCreateRequest request) {
        if (request == null) {
            return null;
        }

        final UserEntity entity = new UserEntity();
        entity.setEmail(request.getEmail());
        entity.setSex(request.getSex());
        return entity;
    }
}
