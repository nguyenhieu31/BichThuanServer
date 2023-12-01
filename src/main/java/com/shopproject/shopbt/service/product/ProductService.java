package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.response.Product_findbyid;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public interface ProductService {
    void create_Product(ProductsDTO productsDTO);

    ProductsDTO findProductById(Long id);

    void update_Product(ProductsDTO productsDTO);

    void delete_Product(Long id);
    Set<ProductsDTO> findALLByLimitOffset(Pageable pageable);
    Set<ProductsDTO> findProductsByCategoryId(Long id);

    Set<ProductsDTO> findByPriceBetweenPrice(BigDecimal startPrice,BigDecimal endPrice);

    Set<ProductsDTO> findProductFeature();

    Set<ProductsDTO> findByNameLikeIgnoreCase(String firstTwoCharacters);

    String getFirstTwoWordsFromProductName(String productName);
    Set<ProductsDTO> findAllProductByCategoryName(Pageable pageable,String categoryName);
}
