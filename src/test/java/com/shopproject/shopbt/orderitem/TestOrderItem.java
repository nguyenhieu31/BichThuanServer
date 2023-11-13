package com.shopproject.shopbt.orderitem;

import com.shopproject.shopbt.dto.OrderItemsDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.dto.UsersDTO;
import com.shopproject.shopbt.service.orderitem.OrderItemService;
import com.shopproject.shopbt.service.product.ProductService;
import com.shopproject.shopbt.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestOrderItem {

    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductService productService;
    @Autowired
    private UserService userService;

    @Test
    void create(){
        // Item 1
        OrderItemsDTO orderItemsDTO1 = new OrderItemsDTO();
        ProductsDTO productsDTO1 = productService.getProductById(18L);
        BigDecimal price1 = productsDTO1.getPrice();
        int quantity1 = 2;
        price1 = price1.multiply(BigDecimal.valueOf(quantity1));
        orderItemsDTO1.setOrderId(6L);
        orderItemsDTO1.setPricePerUnit(price1);
        orderItemsDTO1.setQuantity(quantity1);
        orderItemsDTO1.setProductId(productsDTO1.getProductId());

        // Item 2
        OrderItemsDTO orderItemsDTO2 = new OrderItemsDTO();
        ProductsDTO productsDTO2 = productService.getProductById(19L);
        BigDecimal price2 = productsDTO2.getPrice();
        int quantity2 = 3;
        price2 = price2.multiply(BigDecimal.valueOf(quantity2));
        orderItemsDTO2.setQuantity(quantity2);
        orderItemsDTO2.setPricePerUnit(price2);
        orderItemsDTO2.setOrderId(6L);
        orderItemsDTO2.setProductId(productsDTO2.getProductId());

        // Item 3
        OrderItemsDTO orderItemsDTO3 = new OrderItemsDTO();
        ProductsDTO productsDTO3 = productService.getProductById(20L);
        BigDecimal price3 = productsDTO3.getPrice();
        int quantity3 = 1;
        price3 = price3.multiply(BigDecimal.valueOf(quantity3));
        orderItemsDTO3.setQuantity(quantity3);
        orderItemsDTO3.setPricePerUnit(price3);
        orderItemsDTO3.setOrderId(6L);
        orderItemsDTO3.setProductId(productsDTO3.getProductId());


        Set<OrderItemsDTO> orderItemsDTOS = new HashSet<>();
        orderItemsDTOS.add(orderItemsDTO1);
        orderItemsDTOS.add(orderItemsDTO2);
        orderItemsDTOS.add(orderItemsDTO3);

        orderItemService.create(orderItemsDTOS);
    }

    @Test
    void findAllByOrderId(){
        String username = "user1234";
        List<UsersDTO> usersDTOs = userService.getByUserName(username);
        Set<Long> orderIds = new HashSet<>();
        usersDTOs.forEach(usersDTO -> {
            orderIds.add(usersDTO.getOrderid());
        });

        Set<OrderItemsDTO> orderItemsDTOS = new HashSet<>();

        orderIds.forEach(orderId -> {
            Set<OrderItemsDTO> item = orderItemService.findAllByOrderId(orderId);

            orderItemsDTOS.addAll(item);
        });

        System.out.println(orderItemsDTOS.size());
    }
}
