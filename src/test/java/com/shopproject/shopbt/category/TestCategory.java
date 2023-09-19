package com.shopproject.shopbt.category;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.service.catrgory.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public class TestCategory {
    @Autowired
    private CategoryService categoryService;

    @Test
    void create(){
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        categoriesDTO.setName("Jean");
        categoryService.create_Category(categoriesDTO);
    }

    @Test
    void findById(){
        Long id = 1L;
        CategoriesDTO categoriesDTO = categoryService.findByCategoryId(id);
        System.out.println(categoriesDTO.getCategoryId());
        System.out.println(categoriesDTO.getName());
    }

    @Test
    void update(){
        Long id = 1L;
        CategoriesDTO categoriesDTO = categoryService.findByCategoryId(id);
        categoriesDTO.setName("Dress");
        categoryService.update_Category(categoriesDTO);
    }

    @Test
    void delete(){
        Long id = 1L;
        categoryService.delete_CategoryById(id);
    }

    @Test
    void findAllCategory(){
        Set<CategoriesDTO> categories = categoryService.findAllCategory();
        categories.forEach(category -> {
            System.out.println(category.getCategoryId());
            System.out.println(category.getName());
        });
    }
}
