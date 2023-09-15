package com.shopproject.shopbt.product;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.service.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestProduct {
    @Autowired
    private ProductService productService;

    @Test
    void create() throws IOException {
        ProductsDTO productsDTO = new ProductsDTO();
        productsDTO.setCreatedBy("Hieu");
        productsDTO.setName("Strawberry Midi Dress");
        productsDTO.setDescription("vest name...");
        MultipartFile file = new MockMultipartFile("1.jpg", new FileInputStream(new File("D:\\dev-spring-boot\\image\\1.jpg")));
        productsDTO.setImage(file.getBytes());
        productsDTO.setPrice(BigDecimal.valueOf(100000));
        productsDTO.setMaterial("Đũi");
        productsDTO.setQuantity(100);
        Long cateId = 3L;
        productsDTO.setCategoryId(cateId);
        Set<Integer> colorId = new HashSet<>();
        colorId.add(1);
        colorId.add(2);
        productsDTO.setColorId(colorId);
        Set<Integer> sizeId = new HashSet<>();
        colorId.add(1);
        colorId.add(2);
        productsDTO.setSizeId(sizeId);
        productsDTO.setUpdatedBy("An");
        productService.create_Product(productsDTO);
    }

    @Test
    void findById(){
        Long id = 2L;
        ProductsDTO product = productService.findProductById(id);
        System.out.println(product.getName());
        System.out.println(product.getDescription());
        System.out.println(product.getCategoryId());
    }

    @Test
    void update() throws IOException {
        Long id = 4L;
        ProductsDTO productDto = productService.findProductById(id);
        MultipartFile file = new MockMultipartFile("2.jpg", new FileInputStream(new File("D:\\dev-spring-boot\\image\\2.jpg")));
        productDto.setImage(file.getBytes());
        productService.update_Product(productDto);
    }

    @Test
    void delete(){
        productService.delete_Product(2L);
    }

    @Test
    void findProductByCateId(){
        Long id = 3L;
        Set<ProductsDTO> productsDTOS = productService.findProductsByCategoryId(id);
        productsDTOS.forEach(productsDTO -> {
            System.out.println(productsDTO.getProductId());
            System.out.println(productsDTO.getName());
        });
    }
}
