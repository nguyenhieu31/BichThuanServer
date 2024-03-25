package com.shopproject.shopbt.controller.web;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.response.Product_findbyid;
import com.shopproject.shopbt.response.Product_home;
import com.shopproject.shopbt.service.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/web")
public class HomeController {
    private ProductService productService;
    @GetMapping("/home")
    public ResponseEntity<?> home(){
        try{
            // New Collection
            Pageable pageable= new OffsetBasedPageRequest(0,4, Sort.Direction.ASC,"createdAt");
            Set<ProductsDTO> products_new= productService.findALLByLimitOffset(pageable);
            // Best Selling
            BigDecimal start_price = BigDecimal.valueOf(500000);
            BigDecimal end_price = BigDecimal.valueOf(900000);
            Set<ProductsDTO> products_selling = productService.findByPriceBetweenPrice(start_price, end_price);
            // Featured
            Set<ProductsDTO> products_featured = productService.findProductFeature();
            return ResponseEntity.status(200).body(Product_home.builder()
                            .products_new(products_new)
                            .products_selling(products_selling)
                            .products_featured(products_featured)
                    .build()
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findByProductId(@PathVariable("id") Long id){
        try{
            ProductsDTO product = productService.findProductById(id);
            String name = productService.getFirstTwoWordsFromProductName(product.getName());
            Set<ProductsDTO> products_same = productService.findByNameLikeIgnoreCase(name);
            Set<ProductsDTO> productNotDuplicates = products_same.stream()
                    .filter(productDTO -> !productDTO.getProductId().equals(product.getProductId()))
                    .limit(4)
                    .collect(Collectors.toSet());
            return ResponseEntity.status(200).body(Product_findbyid.builder()
                    .product(product)
                    .productSame(productNotDuplicates).build());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
