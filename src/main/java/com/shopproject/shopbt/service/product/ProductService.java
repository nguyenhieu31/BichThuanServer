package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.response.Product_findbyid;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface ProductService {
    void create_Product(ProductsDTO productsDTO);

    Product_findbyid findProductById(Long id) throws ProductException;

    void update_Product(ProductsDTO productsDTO);

    void delete_Product(Long id);
    Set<ProductsDTO> findALLByLimitOffset(Pageable pageable);
    Set<ProductsDTO> findProductsByCategoryId(Long id);
    Set<ProductsDTO> findAllProduct(int limit,int offset);

    Set<ProductsDTO> findTop10ByPriceBetween(BigDecimal startPrice,BigDecimal endPrice);

    Set<ProductsDTO> findTop10ByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Set<ProductsDTO> findProductFeature();

    Set<ProductsDTO> findByNameLikeIgnoreCase(String firstTwoCharacters);

    String getFirstTwoWordsFromProductName(String productName);

}
