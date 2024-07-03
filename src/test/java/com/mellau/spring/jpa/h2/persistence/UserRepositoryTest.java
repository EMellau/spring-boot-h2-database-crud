package com.mellau.spring.jpa.h2.persistence;

import com.mellau.spring.jpa.h2.common.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();  // Ensure the database is empty before each test

        user1 = new UserEntity();
        user1.setEmail("user1@example.com");
        user1.setLevel(1);
        user1.setGold(100);
        user1.setSex(Sex.MALE);
        userRepository.save(user1);

        user2 = new UserEntity();
        user2.setEmail("user2@example.com");
        user2.setLevel(2);
        user2.setGold(200);
        user2.setSex(Sex.FEMALE);
        userRepository.save(user2);
    }

    @Test
    public void testCreateUser() {
        UserEntity user3 = new UserEntity();
        user3.setEmail("user3@example.com");
        user3.setLevel(3);
        user3.setGold(300);
        user3.setSex(Sex.MALE);

        UserEntity savedUser = userRepository.save(user3);
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("user3@example.com");
        assertThat(savedUser.getLevel()).isEqualTo(3);
        assertThat(savedUser.getGold()).isEqualTo(300);
        assertThat(savedUser.getSex()).isEqualTo(Sex.MALE);
    }

    @Test
    public void testFindAllByEmail() {
        List<UserEntity> users = userRepository.findAllByEmail("user1@example.com");
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    public void testFindAllByOrderByGoldAsc() {
        List<UserEntity> users = userRepository.findAllByOrderByGoldAsc();
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getGold()).isEqualTo(100);
        assertThat(users.get(1).getGold()).isEqualTo(200);
    }

    @Test
    public void testAddGoldByUserId() { // TODO: Refactor method/endpoint
        userRepository.addGoldByUserId(user1.getId(), 50);
        UserEntity updatedUser = userRepository.findById(user1.getId()).orElseThrow();
        assertThat(updatedUser.getGold()).isEqualTo(150);
    }

    @Test
    public void testGetUserById() {
        UserEntity foundUser = userRepository.findById(user1.getId()).orElseThrow();
        assertThat(foundUser.getId()).isEqualTo(user1.getId());
        assertThat(foundUser.getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    public void testDeleteById() {
        userRepository.deleteById(user1.getId());
        assertThat(userRepository.findById(user1.getId())).isEmpty();
    }
}
