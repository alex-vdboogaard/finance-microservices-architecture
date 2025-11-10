package com.finance.accountservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finance.accountservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long userId);

    User findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.userId = :id")
    Optional<User> findByUserIdWithAccounts(@Param("id") Long id);
}
