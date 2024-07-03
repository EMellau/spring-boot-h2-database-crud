package com.mellau.spring.jpa.h2.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Find all users matching a given email
     *
     * @param email value of the email to look for
     * @return List of users
     */
    List<UserEntity> findAllByEmail(String email);

    /**
     * Find all users order by gold
     *
     * @return List of users
     */
    List<UserEntity> findAllByOrderByGoldAsc();

    /**
     * Add gold to user by id
     *
     * @param userId     user to find by id
     * @param goldIncome gold to add to it's gold
     */
    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u " +
            "SET u.gold = u.gold + :goldIncome " +
            "WHERE u.id = :userId")
    Optional<UserEntity> addGoldByUserId(@Param("userId") Long userId, @Param("goldIncome") Integer goldIncome);
}
