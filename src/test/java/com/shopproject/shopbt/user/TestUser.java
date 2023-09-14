package com.shopproject.shopbt.user;

import com.shopproject.shopbt.dto.UsersDTO;
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

    @Test
    void create(){
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setFirstName("van");
        usersDTO.setLastName("hieu");
        usersDTO.setPhoneNumber("0327443323");
        usersDTO.setAddress("Dien Phuong");
        userService.create_User(usersDTO);
    }

    @Test
    void findById(){
        Long id = 3L;
        UsersDTO usersDTO = userService.findByUserId(id);
        System.out.println(usersDTO.getFirstName()+" "+usersDTO.getLastName());
        System.out.println(usersDTO.getPhoneNumber());
        System.out.println(usersDTO.getAddress());
    }

    @Test
    void update(){
        Long id = 3L;
        UsersDTO usersDTO = userService.findByUserId(id);
        usersDTO.setFirstName("Phuoc");
        usersDTO.setLastName("Duc");
        userService.update_User(usersDTO);
    }

    @Test
    void delete(){
        Long id = 4L;
        userService.delete_UserById(id);
    }

    @Test
    void findUsersByToday(){
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        Set<UsersDTO> usersDTOS = userService.findUsersByToday(start,end);

        usersDTOS.forEach(usersDTO -> {
            System.out.println(usersDTO.getFirstName()+" "+usersDTO.getLastName());
            System.out.println(usersDTO.getAddress());
            System.out.println(usersDTO.getPhoneNumber());
        });
    }
}
