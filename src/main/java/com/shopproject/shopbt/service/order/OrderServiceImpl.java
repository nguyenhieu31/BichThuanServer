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
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
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

    @Override
    public List<OrdersDTO> getAllOrderByStatus(Long status) throws Exception {
        try{
            if(status==6){
                return orderRepository.findAllOrder();
            }
            return orderRepository.findOrderByStatus(status);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String handleOrder(Set<String> request) throws Exception {
        try{
            request.forEach(orderCode->{
                Optional<OrdersDTO> isOrder= orderRepository.findOrderByOrderCode(orderCode,0);
                if(isOrder.isPresent()){
                    OrdersDTO ordersDTO=isOrder.get();
                    ordersDTO.setStatus(1);
                    orderRepository.save(readOrderDTO(ordersDTO));
                }
            });
            return "Xử lý đơn hàng thành công";
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String updateStatusOrder(int status, Set<String> request) throws Exception {
        try{
            request.forEach(orderCode->{
                Optional<OrdersDTO> findOrder= orderRepository.findOrderByOrderCode(orderCode,status);
                if(findOrder.isPresent()){
                    
                }
            });
            return null;
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
            UUID randomCode= UUID.randomUUID();
            Order orderGenerator= Order.builder()
                    .orderCode(randomCode)
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
        ordersDTO.setCreatedAt(order.getCreatedAt());
        ordersDTO.setOrderId(order.getOrderId());
        ordersDTO.setUpdatedAt(order.getUpdatedAt());
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
    public List<OrdersDTO> findAllOrder() throws Exception {
        try{
            return orderRepository.findAllOrder();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public Set<OrdersDTO> findALLByOrderToday() {
        return orderRepository.findALLByOrderToday();
    }

    @Override
    public Set<OrdersDTO> findAllOrderBy7Day() {
        return orderRepository.findAllOrderBy7Day();
    }

    @Override
    public BigDecimal totalIncomeToday() {
        return orderRepository.totalIncomeToday();
    }

    @Override
    public Integer quantityOrdersToday() {
        return orderRepository.quantityOrdersToday();
    }

    @Override
    public List<Integer> quantityPersonOrderInChart() {
        List<Map<Integer, Integer>> data = orderRepository.quantityPersonOrderInChart();

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