package com.shopproject.shopbt.repository.user;

import com.shopproject.shopbt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Set<User> findUsersByCreateAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
