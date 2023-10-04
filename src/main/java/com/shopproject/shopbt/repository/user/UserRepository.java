package com.shopproject.shopbt.repository.user;

import com.shopproject.shopbt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByPhoneNumber(String number);
    Set<User> findUsersByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
