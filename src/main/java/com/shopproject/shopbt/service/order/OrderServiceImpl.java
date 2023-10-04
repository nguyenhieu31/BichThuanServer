package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Order order = modelMapper.map(ordersDTO, Order.class);
        User user = userRepository.findById(ordersDTO.getUserId()).get();
        order.setUser(user);

        orderRepository.save(order);
    }

    @Override
    public OrdersDTO findOrderById(Long id) {
        return modelMapper.map(orderRepository.findById(id).get(), OrdersDTO.class);
    }

    @Override
    public void update_Order(OrdersDTO ordersDTO) {
        Order order = modelMapper.map(ordersDTO, Order.class);
        User user = userRepository.findById(ordersDTO.getUserId()).get();
        order.setUser(user);

        orderRepository.save(order);
    }

    @Override
    public void delete_OrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Set<OrdersDTO> findOrdersByUserId(Long id) {
        Set<Order> orders = orderRepository.findOrdersByUser_Userid(id);
        return orders.stream().map(order -> modelMapper.map(order, OrdersDTO.class)).collect(Collectors.toSet());
    }

    @Override
    public Set<OrdersDTO> findOrdersByToday(LocalDateTime startDate, LocalDateTime endDate) {
        Set<Order> orders = orderRepository.findOrdersByCreatedAtBetween(startDate, endDate);
        return orders.stream().map(order -> modelMapper.map(order, OrdersDTO.class)).collect(Collectors.toSet());
    }
}
