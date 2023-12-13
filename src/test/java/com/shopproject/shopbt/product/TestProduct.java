package com.shopproject.shopbt.product;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.service.product.ProductService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestProduct {
    @Autowired
    private ProductService productService;

    @Test
    void create() throws IOException {
//        ProductsDTO productsDTO = new ProductsDTO();
//        productsDTO.setCreatedBy("Hieu");
//        productsDTO.setName("Áo Sơ Mi OLIVER Shirt");
//        productsDTO.setDescription("Chất liệu thoáng mát, thiết kế trẻ trung hiện đại [BST EARLY SUMMER 2023] Áo có chất vải Cotton 100%, kiểu dáng trẻ trung trendy, chất liệu co giãn ...");
//        MultipartFile file = new MockMultipartFile("1.jpg", new FileInputStream(new File("D:\\dev-spring-boot\\image\\1.jpg")));
////        productsDTO.setImage(file.getBytes());
//        productsDTO.setPrice(BigDecimal.valueOf(100000));
//        productsDTO.setMaterial("Áo có chất vải Cotton 100%");
//        productsDTO.setQuantity(100);
//        Long cateId = 3L;
//        productsDTO.setCategoryId(cateId);
//        Set<Integer> colorId = new HashSet<>();
//        colorId.add(1);
//        colorId.add(2);
//        productsDTO.setColorId(colorId);
//        Set<Integer> sizeId = new HashSet<>();
//        sizeId.add(1);
//        sizeId.add(2);
//        productsDTO.setSizeId(sizeId);
//        productsDTO.setUpdatedBy("Hieu");
//        productService.create_Product(productsDTO);
    }

    @Test
    void findById() throws JSONException {
//        Long id = 4L;
//        ProductsDTO product = productService.findProductById(id);
//        System.out.println(product.getProductId());
//       System.out.println(product.getName());
//        String name = productService.getFirstTwoWordsFromProductName(product.getName());
//        Set<ProductsDTO> productsDTOS = productService.findByNameLikeIgnoreCase(name);
//        productsDTOS.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//        });
    }

    @Test
    void update() throws IOException {
//        Long id = 6L;
//        ProductsDTO productDto = productService.findProductById(id);
//        productDto.setPrice(BigDecimal.valueOf(300000));
//
//        productService.update_Product(productDto);
    }

    @Test
    void delete(){
//        productService.delete_Product(2L);
    }

    @Test
    void findProductByCateId(){
        Long id = 1L;int page = 0;
//        Page<ProductsDTO> productsDTOS = productService.findProductsByCategoryId(id, page);
//        productsDTOS.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//            System.out.println(productsDTO.getColorId().size());
//        });
    }

    @Test
    void findTop10ByCreatedAtBetween(){
//        LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay();
//        LocalDateTime start = now.plusDays(-5);
//        LocalDateTime end = now.plusDays(5);
//        Set<ProductsDTO> productsDTOS = productService.findTop10ByCreatedAtBetween(start,end);
//        productsDTOS.forEach(productsDTO -> {
//            System.out.println(productsDTO.getName());
//        });
    }

    @Test
    void findProductsByPriceBetween(){
//        BigDecimal start = BigDecimal.valueOf(500000);
//        BigDecimal end = BigDecimal.valueOf(550000);
//
//        Set<ProductsDTO> products = productService.findByPriceBetweenPrice(start, end);
//        products.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//            System.out.println(productsDTO.getPrice());
//        });
    }


    @Test
    void findProductsByCategoryId(){
//        Long id = 2L;
//        Set<ProductsDTO> products = productService.findProductsByCategoryId(id);
//        products.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//            System.out.println(productsDTO.getPrice());
//        });
    }

    @Test
    void findALLByLimitOffset(){
//        Pageable pageable=null;
//        Set<ProductsDTO> products = productService.findALLByLimitOffset(null);
//        products.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//            System.out.println(productsDTO.getPrice());
//        });
    }

    @Test
    void findProductFeature(){
//        Set<ProductsDTO> products = productService.findProductFeature();
//        products.forEach(productsDTO -> {
//            System.out.println(productsDTO.getProductId());
//            System.out.println(productsDTO.getName());
//            System.out.println(productsDTO.getPrice());
//        });
    }

    @Test
    void findProductByLimitUpdateAt(){
        Pageable pageable= new OffsetBasedPageRequest(0,5, Sort.Direction.ASC,"updatedAt");
        Set<ProductsDTO> productsDTOS = productService.findALLByLimitOffset(pageable);

        productsDTOS.forEach(productsDTO -> {
            System.out.println(productsDTO.getUpdatedAt());
        });
    }
}
