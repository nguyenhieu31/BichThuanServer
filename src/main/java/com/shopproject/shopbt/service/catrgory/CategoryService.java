package com.shopproject.shopbt.service.catrgory;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.ProductsDTO;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void create_Category(CategoriesDTO categoriesDTO);

    CategoriesDTO findByCategoryId(Long id);

    void update_Category(CategoriesDTO categoriesDTO);

    void delete_CategoryById(Long id);

    Set<CategoriesDTO> findAllCategory();
}
