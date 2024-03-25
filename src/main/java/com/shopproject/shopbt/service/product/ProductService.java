package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.dto.ProductsDTO;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ProductService {
    void create_Product(ProductsDTO productsDTO);

    ProductsDTO findProductById(Long id);

    void update_Product(ProductsDTO productsDTO);

    void delete_Product(Long id);
    Set<ProductsDTO> findALLByLimitOffset(Pageable pageable);
    Set<ProductsDTO> findProductsByCategoryId(Long id);

    Set<ProductsDTO> findByPriceBetweenPrice(BigDecimal startPrice,BigDecimal endPrice);
    Set<ProductsDTO> findTop10ByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Set<ProductsDTO> findProductFeature();

    Set<ProductsDTO> findByNameLikeIgnoreCase(String firstTwoCharacters);

    String getFirstTwoWordsFromProductName(String productName);
    Set<ProductsDTO> findAllProductByCategoryName(Pageable pageable,String categoryName);
    ProductsDTO getProductById(Long id);
    List<ProductsDTO> findAllProduct();
}
