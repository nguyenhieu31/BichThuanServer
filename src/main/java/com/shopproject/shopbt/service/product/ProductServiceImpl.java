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
import java.time.LocalDateTime;
import java.util.*;
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
        ProductsDTO productsDTO = new ProductsDTO();
        productsDTO = readProduct(product, productsDTO);
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

    public ProductsDTO readProduct(Product product,ProductsDTO productsDTO){
        productsDTO.setProductId(product.getProductId());
        productsDTO.setCreatedBy(product.getCreatedBy());
        productsDTO.setCreatedAt(product.getCreatedAt());
        productsDTO.setName(product.getName());
        productsDTO.setCategoryId(product.getCategory().getCategoryId());
        // convert byte to string base64
        byte[] imageBytes = product.getImage();
        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
        productsDTO.setImageBase64(imageBase64);

        productsDTO.setDescription(product.getDescription());
        productsDTO.setPrice(product.getPrice());
        productsDTO.setQuantity(product.getQuantity());
        productsDTO.setMaterial(product.getMaterial());

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
        productsDTO.setUpdatedAt(product.getUpdatedAt());
        productsDTO.setUpdatedBy(product.getUpdatedBy());
        return productsDTO;
    }
    @Override
    public void delete_Product(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Set<ProductsDTO> findProductsByCategoryId(Long id) {
        Set<Product> products = productRepository.findProductsByCategory_CategoryId(id);
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });

        return productsDTOS;
    }

    @Override
    public Set<ProductsDTO> findTop10ByPriceBetween(BigDecimal startPrice, BigDecimal endPrice) {
        Set<Product> products = productRepository.findTop10ByPriceBetween(startPrice,endPrice);
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });
        return productsDTOS;
    }

    @Override
    public Set<ProductsDTO> findTop10ByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        Set<Product> products = productRepository.findTop10ByCreatedAtBetween(startDate,endDate);
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });
        return productsDTOS;
    }

    @Override
    public Set<ProductsDTO> findTop10() {
        Set<Product> products = productRepository.findAll().stream().limit(10).collect(Collectors.toSet());
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });
        return productsDTOS;
    }

    @Override
    public Set<ProductsDTO> findByNameLikeIgnoreCase(String firstTwoCharacters) {
        Set<Product> products = productRepository.findByNameLikeIgnoreCase("%" +  firstTwoCharacters + "%");
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });
        return productsDTOS;
    }

    public String getFirstTwoWordsFromProductName(String productName) {
        if (productName == null) {
            return null;
        }
        // Chia chuỗi thành các từ
        String[] words = productName.split("\\s+");

        if (words.length >= 2) {
            // Lấy hai từ đầu tiên và kết hợp chúng thành một chuỗi
            return words[0] + " " + words[1];
        } else {
            // Trường hợp có ít hơn hai từ, trả lại chuỗi ban đầu
            return productName;
        }
    }

}
