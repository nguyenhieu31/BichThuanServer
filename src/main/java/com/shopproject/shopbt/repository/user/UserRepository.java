package com.shopproject.shopbt.repository.user;

import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByPhoneNumber(String number);
    Set<User> findUsersByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    @Query("select new com.shopproject.shopbt.dto.UsersDTO(u.userid,  o.oderId) " +
            "from User u " +
            "join Order o on o.user.userid = u.userid " +
            "where u.userName = :username ")
    List<UsersDTO> getByUserName(@Param("username") String username);

    @Query("select new com.shopproject.shopbt.dto.UsersDTO(u.userid, u.fullName, u.userName, u.phoneNumber, u.email, u.role) " +
            "from User u " +
            "where date(u.createdAt) = current_date")
    Set<UsersDTO> findAllUserRegisterByToday();

    @Query("select new com.shopproject.shopbt.dto.UsersDTO(u.userid, u.fullName, u.userName, u.phoneNumber, u.email, u.role) " +
            "from User u " +
            "where date(u.createdAt) between current_date - 7  and current_date")
    Set<UsersDTO> findAllUserRegisterBy7Days();

    @Query("select new com.shopproject.shopbt.dto.UsersDTO(u.userid) from User u where u.userName = :name")
    Optional<UsersDTO> getUserIdByUserName(@Param("name") String name);
}
