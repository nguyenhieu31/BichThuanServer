package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.CartsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserService {
    void create_User(UsersDTO usersDTO);

    UsersDTO findByUserId(Long id);

    void update_User(UsersDTO usersDTO);

    void delete_UserById(Long id);

    Set<UsersDTO> findUsersByToday(LocalDateTime startDate, LocalDateTime endDate);

    CartsDTO createCartIfNotExists(Long userId);

    List<UsersDTO> getByUserName(String username);

    Set<UsersDTO> findAllUserRegisterByToday();

    Set<UsersDTO> findAllUserRegisterBy7Days();

    UsersDTO findUserIdByUserName(String name);
}
