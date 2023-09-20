package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.respon.Product_findbyid;
import com.shopproject.shopbt.respon.Product_home;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@AllArgsConstructor
public class HomeController {
    private ProductService productService;

    @GetMapping("/home")
    public ResponseEntity<Product_home> home(){
        // New Collection
        LocalDateTime now = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime start_date = now.plusDays(-5);
        LocalDateTime end_date = now.plusDays(5);
        Set<ProductsDTO> products_new = productService.findTop10ByCreatedAtBetween(start_date, end_date);


        // Best Selling
        BigDecimal start_price = BigDecimal.valueOf(200000);
        BigDecimal end_price = BigDecimal.valueOf(400000);
        Set<ProductsDTO> products_selling = productService.findTop10ByPriceBetween(start_price,end_price);

        // Featured
        Set<ProductsDTO> products_featured = productService.findTop10();

        return ResponseEntity.status(HttpStatus.OK).body(Product_home.builder()
                .products_new(products_new)
                .products_selling(products_selling)
                .products_featured(products_featured)
                .build());
    }

    @GetMapping("product/{id}")
    public ResponseEntity<Product_findbyid> findByProductId(@PathVariable("id") Long id){
        ProductsDTO productsDTO = productService.findProductById(id);
        Set<ProductsDTO> products_same = productService.findAllByNameLike(productsDTO.getName());
        return ResponseEntity.status(HttpStatus.OK).body(Product_findbyid.builder()
                .productsDTO(productsDTO)
                .products_same(products_same).build());
    }


}
