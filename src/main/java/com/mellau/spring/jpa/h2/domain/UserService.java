package com.mellau.spring.jpa.h2.domain;

import com.mellau.spring.jpa.h2.domain.model.User;
import com.mellau.spring.jpa.h2.domain.model.UserCreateRequest;
import com.mellau.spring.jpa.h2.domain.model.UserSearchRequest;
import com.mellau.spring.jpa.h2.domain.model.UserUpdateRequest;
import com.mellau.spring.jpa.h2.error.NotFoundException;
import com.mellau.spring.jpa.h2.persistence.UserEntityMapper;
import com.mellau.spring.jpa.h2.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mellau.spring.jpa.h2.error.LogCommonError.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityMapper mapper;
    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return mapper.toUser(userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, "id", id)));
    }

    public List<User> searchUser(UserSearchRequest search) {
        return mapper.toUsers(userRepository.findAllByEmail(search.getEmail()));
    }

    public User createUser(UserCreateRequest userCreateRequest) {
        return mapper.toUser(userRepository.save(mapper.toEntity(userCreateRequest)));
    }

    public List<User> getRanking() {
        return mapper.toUsers(userRepository.findAllByOrderByGoldAsc());
    }

    public User updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        //final var user = getUserById(userId);

        // TODO change the repo method for a gold update more than an add. still do it custom?

        return mapper.toUser(userRepository.addGoldByUserId(userId, userUpdateRequest.getGold())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND, "id", userId)));
    }

    public void deleteById(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(NOT_FOUND, "id", id);
        }
    }
}
