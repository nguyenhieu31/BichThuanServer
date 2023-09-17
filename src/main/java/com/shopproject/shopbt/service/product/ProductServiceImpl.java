package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.Categories;
import com.shopproject.shopbt.entity.Color;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.entity.Size;
import com.shopproject.shopbt.repository.category.CategoryRepository;
import com.shopproject.shopbt.repository.color.ColorRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.size.SizeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ColorRepository colorRepository;
    private SizeRepository sizeRepository;
    private ModelMapper modelMapper;

    @Override
    public void create_Product(ProductsDTO productsDTO) {
        Product product = new Product();
        product = readProductDTO(product, productsDTO);
        productRepository.save(product);
    }

    @Override
    public ProductsDTO findProductById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductsDTO productsDTO = modelMapper.map(product, ProductsDTO.class);
        Set<Integer> colorIds = new HashSet<>();
        Set<Color> colors = product.getColors();
        colors.forEach(colorId -> {
            colorIds.add(Math.toIntExact(colorId.getColorId()));
        });
        productsDTO.setColorId(colorIds);

        Set<Integer> sizeIds = new HashSet<>();
        Set<Size> sizes = product.getSizes();
        sizes.forEach(sizeId -> {
            sizeIds.add(Math.toIntExact(sizeId.getSizeId()));
        });
        productsDTO.setSizeId(sizeIds);
        return productsDTO;
    }

    @Override
    public void update_Product(ProductsDTO productsDTO) {
        Product product = productRepository.findById(productsDTO.getProductId()).get();
        product = readProductDTO(product,productsDTO);

        productRepository.save(product);
    }

    public Product readProductDTO(Product product,ProductsDTO productsDTO){
        product.setCreatedBy(productsDTO.getCreatedBy());
        product.setName(productsDTO.getName());
        product.setDescription(productsDTO.getDescription());
        product.setImage(productsDTO.getImage());
        product.setPrice(productsDTO.getPrice());
        product.setMaterial(productsDTO.getMaterial());
        product.setQuantity(productsDTO.getQuantity());
        Categories category = categoryRepository.findCategoriesByCategoryId(productsDTO.getCategoryId());
        product.setCategory(category);

        // add set colors
        Set<Integer> colorIds = productsDTO.getColorId();
        Set<Color> colors = new HashSet<>();
        colorIds.forEach(colorId -> {
            colors.add(colorRepository.findByColorId(colorId));
        });
        product.setColors(colors);


        // add set sizes
        Set<Integer> sizeIds = productsDTO.getSizeId();
        Set<Size> sizes = new HashSet<>();
        sizeIds.forEach(sizeId -> {
            sizes.add(sizeRepository.findBySizeId(sizeId));
        });
        product.setSizes(sizes);
        product.setUpdatedBy(productsDTO.getUpdatedBy());
        return product;
    }

    @Override
    public void delete_Product(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Set<ProductsDTO> findProductsByCategoryId(Long id) {
        Set<Product> products = productRepository.findProductsByCategory_CategoryId(id);
        return products.stream().map(product -> modelMapper.map(product, ProductsDTO.class)).collect(Collectors.toSet());
    }

    @Override
    public Set<ProductsDTO> findProductsByPriceBetween(BigDecimal startPrice, BigDecimal endPrice) {
        Set<Product> products = productRepository.findProductsByPriceBetween(startPrice,endPrice);
        return products.stream().map(product -> modelMapper.map(product, ProductsDTO.class)).collect(Collectors.toSet());
    }
}
