package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_User(UsersDTO usersDTO) {
        userRepository.save(modelMapper.map(usersDTO, User.class));
    }

    @Override
    public UsersDTO findByUserId(Long id) {
        return modelMapper.map(userRepository.findById(id).get(), UsersDTO.class);
    }

    @Override
    public void update_User(UsersDTO usersDTO) {
        userRepository.save(modelMapper.map(usersDTO, User.class));
    }

    @Override
    public void delete_UserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Set<UsersDTO> findUsersByToday(LocalDateTime startDate, LocalDateTime endDate) {
        Set<User> users = userRepository.findUsersByCreateAtBetween(startDate,endDate);
        return users.stream().map(user -> modelMapper.map(user, UsersDTO.class)).collect(Collectors.toSet());
    }
}
