package com.shopproject.shopbt.controller;

import com.google.api.Http;
import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.ExceptionCustom.RegisterException;
import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.Order;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.CreateOrderRequest;
import com.shopproject.shopbt.response.Product_Detail_Order;
import com.shopproject.shopbt.response.Product_MyOrder;
import com.shopproject.shopbt.dto.OrdersDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.entity.OrderItem;
import com.shopproject.shopbt.response.Product_MyOrder;
import com.shopproject.shopbt.service.Redis.RedisService;
import com.shopproject.shopbt.service.order.OrderService;
import com.shopproject.shopbt.service.orderitem.OrderItemService;
import com.shopproject.shopbt.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/web/order")
public class MyOrderController {
    private OrderItemService orderItemService;
    private final OrderService orderService;
    private Set<Product_Detail_Order> findOrderByType(int type, List<Order> orderList) throws Exception {
        Set<Product_Detail_Order> response= new HashSet<>();
        orderList.forEach(order->{
            if(order.getDeletedAt()!=null && !order.getDeletedAt().isBefore(LocalDateTime.now())){
                orderService.delete_OrderById(order.getOrderId());
            }else if(order.getStatus()==type){
                Set<Product_Detail_Order> findOrderItem= orderItemService.findAllByOrderId(order.getOrderId());
                response.addAll(findOrderItem);
            }
        });
        return response;
    }
    @PostMapping("/create")
    public ResponseEntity<?> CreateOrder(@RequestBody CreateOrderRequest createOrderRequest){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                if(user.getPhoneNumber()!=null){
                    OrdersDTO ordersDTO= orderService.create_Order(createOrderRequest,user);
                    if(ordersDTO!=null){
                        return ResponseEntity.status(HttpStatus.OK).body("Bạn đã đặt hàng thành công!!!");
                    }else{
                        throw new Exception("Đặt hàng thất bại!!!");
                    }
                }else{
                    throw new RegisterException("chưa xác thực số điện thoại");
                }
            }else{
                throw new LoginException("phiên đăng nhập hết hạn");
            }
        } catch (LoginException ex){
            return ResponseEntity.status(403).body(ex.getMessage());
        } catch (RegisterException e){
            return ResponseEntity.status(402).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/my-order")
    public ResponseEntity<?> getMyOrder(@RequestParam("type") int type) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                List<Order> orders= orderService.findOrdersByUserId(user.getUserid());
                Set<Product_Detail_Order> orders_w_confirm = new HashSet<>();
                Set<Product_Detail_Order> orders_w_delivery = new HashSet<>();
                Set<Product_Detail_Order> orders_delivering = new HashSet<>();
                Set<Product_Detail_Order> orders_delivered = new HashSet<>();
                Set<Product_Detail_Order> orders_cancel= new HashSet<>();
                Set<Product_Detail_Order> orders_return = new HashSet<>();
                switch (type) {
                    case 0 -> {
                        orders_w_confirm = findOrderByType(type, orders);
                    }
                    case 1 -> {
                        orders_w_delivery = findOrderByType(type, orders);
                    }
                    case 2 -> {
                        orders_delivering = findOrderByType(type, orders);
                    }
                    case 3 -> {
                        orders_delivered = findOrderByType(type, orders);
                    }
                    case 4 -> {
                        orders_cancel = findOrderByType(type, orders);
                    }
                    case 5 -> {
                        orders_return = findOrderByType(type, orders);
                    }
                }
                return ResponseEntity.status(200).body(Product_MyOrder.builder()
                        .product_w_confirm(orders_w_confirm)
                        .product_w_delivery(orders_w_delivery)
                        .product_delivering(orders_delivering)
                        .product_delivered(orders_delivered)
                        .product_cancel(orders_cancel)
                        .product_return(orders_return)
                        .build());
            }else{
                throw new LoginException("Phiên đăng nhập hết hạn!");
            }
        } catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/cancel-order")
    public ResponseEntity<?> cancelOrder(@RequestParam("orderId") Long orderId,@RequestParam("reasonCancel") String reasonCancel){
        try{     
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                List<Order> userOrders= orderService.findOrdersByUserId(user.getUserid());
                final Order[] order = {null};
                userOrders.forEach(orderItem->{
                    if(orderItem.getOrderId().equals(orderId)){
                        order[0] =orderItem;
                    }
                });
                Order orderResponse= orderService.cancelOrder(order[0], reasonCancel);
                if(orderResponse!=null){
                    return ResponseEntity.status(HttpStatus.OK).body("Hủy Đơn Thành Công!");
                }else{
                    throw new Exception("Lỗi không thể hủy đơn hàng. Vui lòng liên hệ với nhà cung cấp!!");
                }
            }else{
                throw new LoginException("Phiên đăng nhập hết hạn");
            }
        }catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

