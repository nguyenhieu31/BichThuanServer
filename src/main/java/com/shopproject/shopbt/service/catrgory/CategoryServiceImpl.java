package com.shopproject.shopbt.service.catrgory;

import com.shopproject.shopbt.dto.CategoriesDTO;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Categories;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.repository.category.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    @Override
    public void create_Category(CategoriesDTO categoriesDTO) {
        categoryRepository.save(modelMapper.map(categoriesDTO, Categories.class));
    }

    @Override
    public CategoriesDTO findByCategoryId(Long id) {
        return modelMapper.map(categoryRepository.findById(id), CategoriesDTO.class);
    }

    @Override
    public void update_Category(CategoriesDTO categoriesDTO) {
        categoryRepository.save(modelMapper.map(categoriesDTO, Categories.class));
    }

    @Override
    public void delete_CategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Set<CategoriesDTO> findAllCategory() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoriesDTO.class)).collect(Collectors.toSet());
    }
}
