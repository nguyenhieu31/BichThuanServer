package com.shopproject.shopbt.repository.category;

import com.shopproject.shopbt.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    Categories findCategoriesByCategoryId(Long id);
}
