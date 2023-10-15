package com.shopproject.shopbt.controller;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.request.OffsetBasedPageRequest;
import com.shopproject.shopbt.response.Product_findbyid;
import com.shopproject.shopbt.response.Product_home;
import com.shopproject.shopbt.service.product.ProductService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            Pageable pageable= new OffsetBasedPageRequest(0,4, Sort.Direction.DESC,"createdAt");
            Set<ProductsDTO> products_new= productService.findALLByLimitOffset(pageable);
            // Best Selling
            BigDecimal start_price = BigDecimal.valueOf(500000);
            BigDecimal end_price = BigDecimal.valueOf(900000);
            Set<ProductsDTO> products_selling = productService.findTop10ByPriceBetween(start_price,end_price).stream().limit(4).collect(Collectors.toSet());
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
    public ResponseEntity<?> findByProductId(@PathVariable("id") Long id)  {
        try{
            Product_findbyid product= productService.findProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }catch (ProductException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
