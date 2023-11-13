package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    @Override
    public void create_Order(OrdersDTO ordersDTO) {
        Order order = new Order();
        User user = userRepository.findById(ordersDTO.getUserId()).orElseThrow(() -> new UsernameNotFoundException(("User not found")));
        if (user != null){
            order.setUser(user);
        }
        order.setStatus(ordersDTO.getStatus());
        order.setAddress(ordersDTO.getAddress());

        orderRepository.save(order);
    }

    private OrdersDTO readOrder(Order order){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(order.getStatus());
        ordersDTO.setUserId(order.getUser().getUserid());
        ordersDTO.setCreateAt(order.getCreatedAt());
        ordersDTO.setOrderId(order.getOderId());
        ordersDTO.setUpdateAt(order.getUpdatedAt());
        ordersDTO.setAddress(order.getAddress());

        return ordersDTO;
    }

    private Order readOrderDTO(OrdersDTO ordersDTO){
        Order order = new Order();
        order.setOderId(ordersDTO.getOrderId());
        User user = userRepository.findById(ordersDTO.getUserId()).get();
        order.setUser(user);
        order.setStatus(ordersDTO.getStatus());
        order.setCreatedAt(ordersDTO.getCreateAt());
        order.setAddress(ordersDTO.getAddress());
        return order;
    }


    private OrdersDTO ConvertDTO(Object[] order){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrderId((Long) order[0]);
        ordersDTO.setStatus((Integer) order[1]);
        ordersDTO.setCreateAt((LocalDateTime) order[2]);
        return ordersDTO;
    }
    @Override
    public OrdersDTO findOrderById(Long id) {
        return readOrder(orderRepository.findOrderByOrderId(id).orElseThrow(() -> new RuntimeException("Order not found by id : " + id)));
    }

    @Override
    public void update_Order(OrdersDTO ordersDTO) {
        Order order = modelMapper.map(ordersDTO, Order.class);
        User user = userRepository.findById(ordersDTO.getUserId()).get();
        order.setUser(user);

        orderRepository.save(order);

        orderRepository.save(order);
    }

    @Override
    public void delete_OrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Set<OrdersDTO> findOrdersByUserId(Long id) {
        Set<Object[]> orders = orderRepository.findOrdersByUser_Userid(id);
        return orders.stream()
                .map(this::ConvertDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public List<OrdersDTO> findLatestOrders(Pageable pageable) {
        Page<OrdersDTO> ordersPage = orderRepository.findLatestOrders(pageable);

        return ordersPage.getContent();
    }

    @Override
    public Set<OrdersDTO> findALLByOrderToday() {
        return orderRepository.findALLByOrderToday();
    }

    @Override
    public Set<OrdersDTO> findAllOrderBy7Day() {
        return orderRepository.findAllOrderBy7Day();
    }
}
