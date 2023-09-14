package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.UsersDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public interface UserService {
    void create_User(UsersDTO usersDTO);

    UsersDTO findByUserId(Long id);

    void update_User(UsersDTO usersDTO);

    void delete_UserById(Long id);

    Set<UsersDTO> findUsersByToday(LocalDateTime startDate, LocalDateTime endDate);
}
