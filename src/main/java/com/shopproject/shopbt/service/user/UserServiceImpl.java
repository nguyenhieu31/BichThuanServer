package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.CartsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.Address.AddressRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.comment.CommentRepository;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private AddressRepo addressRepo;
    private OrderRepository orderRepository;
    private CommentRepository commentRepository;
    private CartRepository cartRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_User(UsersDTO usersDTO) {
        User user = new User();
        user = readUserDTO(user, usersDTO);

        userRepository.save(user);

        createCartIfNotExists(user.getUserid());
    }

    @Override
    public UsersDTO findByUserId(Long id) {
        User user = userRepository.findById(id).get();
        UsersDTO usersDTO = new UsersDTO();
        usersDTO = readUser(user,usersDTO);
        return usersDTO;
    }

    public User readUserDTO(User user, UsersDTO usersDTO){
        user.setUserName(usersDTO.getUserName());
        user.setFullName(usersDTO.getFullName());
        user.setPassword(usersDTO.getPassword());
        user.setRole(usersDTO.getRole());
        user.setPhoneNumber(usersDTO.getPhoneNumber());

        // address
        Set<Long> addressIds = usersDTO.getAddressIds();
        Set<Address> addresses = new HashSet<>();
        if (addressIds != null){
            addressIds.forEach(addressId -> {
                addresses.add(addressRepo.findById(addressId).get());
            });
        }
        user.setAddresses(addresses);

        // order
        Set<Long> orderIds = usersDTO.getOrderIds();
        Set<Order> orders = new HashSet<>();
        if (orderIds != null){
            orderIds.forEach(orderId -> {
                orders.add(orderRepository.findById(orderId).get());
            });
        }
        user.setOrders(orders);

        // comment
        Set<Long> commentIds = usersDTO.getCommentIds();
        Set<Comment> comments = new HashSet<>();
        if (commentIds != null){
            commentIds.forEach(commentId -> {
                comments.add(commentRepository.findById(commentId).get());
            });
        }
        user.setComments(comments);

        return user;
    }
    public UsersDTO readUser(User user, UsersDTO usersDTO){
        usersDTO.setUserid(user.getUserid());
        usersDTO.setPhoneNumber(user.getPhoneNumber());
        usersDTO.setUserName(user.getUsername());
        usersDTO.setFullName(user.getFullName());
        usersDTO.setPassword(user.getPassword());
        usersDTO.setRole(user.getRole());
        usersDTO.setCreateAt(user.getCreateAt());
        usersDTO.setUpdateAt(user.getUpdateAt());

        // address id
        Set<Long> addressIds = new HashSet<>();
        Set<Address> addresses = user.getAddresses();
        addresses.forEach(address -> {
            addressIds.add(address.getId());
        });
        usersDTO.setAddressIds(addressIds);

        // order id
        Set<Long> orderIds = new HashSet<>();
        Set<Order> orders = user.getOrders();
        orders.forEach(order -> {
            orderIds.add(order.getOderId());
        });
        usersDTO.setOrderIds(orderIds);

        // cart id
        Long id = user.getCart().getCartId();
        usersDTO.setCartId(id);

        // comment id
        Set<Long> commentIds = new HashSet<>();
        Set<Comment> comments = user.getComments();
        comments.forEach(comment -> {
            commentIds.add(comment.getCommentId());
        });
        usersDTO.setCommentIds(commentIds);

        return usersDTO;
    }
    @Override
    public void update_User(UsersDTO usersDTO) {
        User user = userRepository.findById(usersDTO.getUserid()).get();
        user = readUserDTO(user, usersDTO);

        userRepository.save(user);
    }

    @Override
    public void delete_UserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Set<UsersDTO> findUsersByToday(LocalDateTime startDate, LocalDateTime endDate) {
        Set<User> users = userRepository.findUsersByCreatedAtBetween(startDate,endDate);
        return users.stream().map(user -> modelMapper.map(user, UsersDTO.class)).collect(Collectors.toSet());
    }


    public Boolean userHasCart(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Cart cart = user.getCart();
            return cart != null;
        }
        return false;
    }

    @Override
    public CartsDTO createCartIfNotExists(Long userId) {
        if (!userHasCart(userId)) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                Cart cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart);
                return modelMapper.map(cart, CartsDTO.class);
            }
        }
        return null;
    }
}
