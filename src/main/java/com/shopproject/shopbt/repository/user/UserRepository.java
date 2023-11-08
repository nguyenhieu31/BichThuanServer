package com.shopproject.shopbt.repository.user;

import com.shopproject.shopbt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByPhoneNumber(String number);
    Set<User> findUsersByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("select u.email from User u where u.email=:emailUser")
    Optional<String> findByEmail(@Param("emailUser") String email);

}
