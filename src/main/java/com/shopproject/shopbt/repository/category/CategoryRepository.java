package com.shopproject.shopbt.repository.category;

import com.shopproject.shopbt.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Categories findCategoriesByCategoryId(Long id);
}
