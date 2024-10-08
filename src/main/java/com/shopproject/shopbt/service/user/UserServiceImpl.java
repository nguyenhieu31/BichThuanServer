package com.shopproject.shopbt.service.user;

import com.shopproject.shopbt.dto.CartsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.Address.AddressRepo;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.comment.CommentRepository;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
//    private static final Logger logger = LoggerFactory.getLogger(User.class);

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
        usersDTO.setCreateAt(user.getCreatedAt());
        usersDTO.setUpdateAt(user.getUpdatedAt());

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
            orderIds.add(order.getOrderId());
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

    @Override
    public Set<UsersDTO> findAllUserRegisterBy7Days() {
        return userRepository.findAllUserRegisterBy7Days();
    }

    @Override
    public Set<UsersDTO> findAllUserRegisterByToday() {
        return userRepository.findAllUserRegisterByToday();
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
    public void createCartIfNotExists(Long userId) {
        if (!userHasCart(userId)) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                Cart cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart);
                modelMapper.map(cart, CartsDTO.class);
            }
        }
    }

    @Override
    public List<UsersDTO> getByUserName(String username) {
        return userRepository.getByUserName(username);
    }

    @Override
    public UsersDTO findUserIdByUserName(String name) {
        return userRepository.getUserIdByUserName(name).orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public Set<UserResponse> getAllUser() {
//        logger.debug("Calling getAllUser method");
        return userRepository.findAllUser();
    }

    @Override
    public void updateStatusOfUser(boolean status,Long userid,String userName) throws Exception {
        try{
            User findUser=userRepository.findById(userid).orElse(null);
            if(findUser!=null){
                findUser.setActive(status);
                findUser.setUpdatedBy(userName);
                userRepository.save(findUser);
            }else{
                throw new Exception("Không tìm thấy người dùng");
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Integer quantityPersonRegisterToday() {
        return userRepository.quantityPersonRegisterToday();
    }

    @Override
    public List<Integer> quantityPersonRegisterInChart() {
        List<Map<Integer, Integer>> data = userRepository.quantityPersonRegisterInChart();

        List<Integer> result = new ArrayList<>();

        Map<Integer, Integer> monthCountMap = new HashMap<>();
        for (Map<Integer, Integer> entry : data) {
            Integer key = 0, value = 0;
            for (Map.Entry<Integer, Integer> pair : entry.entrySet()) {
                if (Objects.equals(pair.getKey(), "MONTH"))
                {
                    key = pair.getValue();
                } else {
                    value = pair.getValue();
                }
            }
            monthCountMap.put(key, value);
        }

        for (int i = 1; i <= 12; i++) {
            if (monthCountMap.containsKey(i)) {
                result.add(monthCountMap.get(i));
            } else {
                result.add(0);
            }
        }

        return result;
    }
}
