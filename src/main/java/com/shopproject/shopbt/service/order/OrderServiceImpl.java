package com.shopproject.shopbt.service.order;

import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.ProductPayment;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.order.OrderRepository;
import com.shopproject.shopbt.repository.orderitem.OrderItemRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public OrdersDTO findStatusByOrderId(Long id) {
        return orderRepository.findStatusByOrderId(id);
    }

    @Override
    public Order cancelOrder(Order order,String reasonCancel) throws Exception {
        try{
            Set<OrderItem> orderItems= orderItemRepository.findByOrderId(order.getOrderId());
            orderItems.forEach(orderItem -> {
                Product product= orderItem.getProduct();
                product.setQuantity(orderItem.getQuantity()+product.getQuantity());
                productRepository.save(product);
            });
            LocalDateTime now= LocalDateTime.now();
            LocalDateTime futureDate= now.plusDays(7);
            order.setReasonCancelOrder(reasonCancel);
            order.setOrdered(false);
            order.setStatus(4);
            order.setDeletedAt(futureDate);
            return orderRepository.save(order);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Set<Order> findOrderByStatusAndUserId(int type, Long userId) throws Exception {
        try{
            return orderRepository.findByStatusAndUserId(type,userId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private BigDecimal parseBigDecimal(String formattedNumber) {
        try {
            // Loại bỏ ký tự ngăn cách (separator) và chuyển đổi thành BigDecimal
            String cleanedNumber = formattedNumber.replace(".", "").replace(",", "");
            return new BigDecimal(cleanedNumber);
        } catch (NumberFormatException e) {
            // Xử lý exception nếu có lỗi chuyển đổi
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public OrdersDTO create_Order(CreateOrderRequest request, User user) throws Exception {
        try{
            Set<ProductPayment> orderItems= request.getProductsPayment();
            Order orderGenerator= Order.builder()
                    .user(user)
                    .isOrdered(request.isOrdered())
                    .phonePersonOrder(request.getPhonePersonOrder())
                    .status(request.getStatusOrder())
                    .pricePersonPay(parseBigDecimal(request.getPricePersonPay()))
                    .personNote(request.getPersonNote())
                    .address(request.getAddress())
                    .build();
            Order saveOrder= orderRepository.save(orderGenerator);
            orderItems.forEach(orderItem->{
                Product product= productRepository.findByProductId(orderItem.getProductId());
                product.setQuantity(product.getQuantity()-orderItem.getQuantity());
                OrderItem order= OrderItem.builder()
                        .color(orderItem.getColor())
                        .size(orderItem.getSize())
                        .quantity(orderItem.getQuantity())
                        .pricePerUnit(orderItem.getPrice())
                        .order(saveOrder)
                        .product(product)
                        .build();
                productRepository.save(product);
                orderItemRepository.save(order);
            });
            return readOrder(saveOrder);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private OrdersDTO readOrder(Order order){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setStatus(order.getStatus());
        ordersDTO.setUserId(order.getUser().getUserid());
        ordersDTO.setCreateAt(order.getCreatedAt());
        ordersDTO.setOrderId(order.getOrderId());
        ordersDTO.setUpdateAt(order.getUpdatedAt());
        ordersDTO.setAddress(order.getAddress());
        return ordersDTO;
    }

    private Order readOrderDTO(OrdersDTO ordersDTO){
        Order order = new Order();
        order = orderRepository.findOrderByOrderId(ordersDTO.getOrderId()).orElseThrow();
        order.setStatus(ordersDTO.getStatus());
        return order;
    }

//    private OrdersDTO ConvertDTO(Object[] order){
//        OrdersDTO ordersDTO = new OrdersDTO();
//        ordersDTO.setOrderId((Long) order[0]);
//        ordersDTO.setStatus((Integer) order[1]);
//        ordersDTO.setCreateAt((LocalDateTime) order[2]);
//        return ordersDTO;
//    }
    @Override
    public OrdersDTO findOrderById(Long id) {
        return readOrder(orderRepository.findOrderByOrderId(id).orElseThrow(() -> new RuntimeException("Order not found by id : " + id)));
    }

    @Override
    public void update_Order(OrdersDTO ordersDTO) {
        Order order = readOrderDTO(ordersDTO);
        orderRepository.save(order);
    }

    @Override
    public void delete_OrderById(Long id) {
        orderRepository.deleteOrderAndOrderItemsByOrderId(id);
    }

    @Override
    public List<Order> findOrdersByUserId(Long id) {
        return orderRepository.findOrdersByUser_Userid(id);
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