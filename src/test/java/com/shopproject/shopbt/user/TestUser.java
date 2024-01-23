package com.shopproject.shopbt.user;

import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.service.authentication.AuthenticationService;
import com.shopproject.shopbt.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
public class TestUser {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void create(){
//        UsersDTO usersDTO = new UsersDTO();
//        usersDTO.setUserName("tuanvy");
//        usersDTO.setFullName("Nguyễn Viên Tuấn Vỹ");
//        usersDTO.setRole("USER");
//        usersDTO.setPassword("12345");
//        usersDTO.setPhoneNumber("0327443323");
//
//        userService.create_User(usersDTO);
    }

    @Test
    void findById(){
//        Long id = 13L;
//        UsersDTO usersDTO = userService.findByUserId(id);
//        System.out.println(usersDTO.getFullName());
//        System.out.println(usersDTO.getPhoneNumber());
    }
//
//    @Test
//    void update(){
//        Long id = 3L;
//        UsersDTO usersDTO = userService.findByUserId(id);
//        usersDTO.setFirstName("Phuoc");
//        usersDTO.setLastName("Duc");
//        userService.update_User(usersDTO);
//    }
//
//    @Test
//    void delete(){
//        Long id = 4L;
//        userService.delete_UserById(id);
//    }
//
    @Test
    void findUsersByToday(){
//        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
//        LocalDateTime end = start.plusDays(1);
//        Set<UsersDTO> usersDTOS = userService.findUsersByToday(start,end);
//
//        usersDTOS.forEach(usersDTO -> {
//            System.out.println(usersDTO.getFullName());
//            System.out.println(usersDTO.getAddressIds());
//            System.out.println(usersDTO.getPhoneNumber());
//        });
    }

    @Test
    void findAllUserRegisterByToday(){
        Set<UsersDTO> usersDTOS = userService.findAllUserRegisterByToday();

        System.out.println(usersDTOS.size());
    }

    @Test
    void findAllUserRegisterBy7Days(){
        Set<UsersDTO> usersDTOS = userService.findAllUserRegisterBy7Days();

        System.out.println(usersDTOS.size());
    }
}
