package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.response.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserService {
    void create_User(UsersDTO usersDTO);

    UsersDTO findByUserId(Long id);

    void update_User(UsersDTO usersDTO);

    void delete_UserById(Long id);

    Set<UsersDTO> findUsersByToday(LocalDateTime startDate, LocalDateTime endDate);

    void createCartIfNotExists(Long userId);

    List<UsersDTO> getByUserName(String username);

    Set<UsersDTO> findAllUserRegisterByToday();

    Set<UsersDTO> findAllUserRegisterBy7Days();

    UsersDTO findUserIdByUserName(String name);
    Set<UserResponse> getAllUser();
    void updateStatusOfUser(boolean status, Long userId,String userName) throws Exception;
}
