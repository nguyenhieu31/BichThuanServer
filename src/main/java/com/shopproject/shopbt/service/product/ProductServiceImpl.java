package com.shopproject.shopbt.service.product;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductsDTO;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.category.CategoryRepository;
import com.shopproject.shopbt.repository.color.ColorRepository;
import com.shopproject.shopbt.repository.gallery_image.Gallery_ImageRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.size.SizeRepository;
import com.shopproject.shopbt.response.Product_findbyid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
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
    @Override
    public void create_Product(ProductsDTO productsDTO) {
        Product product = new Product();
        product = readProductDTO(product, productsDTO);
        productRepository.save(product);
    }

    @Override
    public Product_findbyid findProductById(Long id) throws ProductException {
        try{
            Product product = productRepository.findById(id).get();
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            String name= getFirstTwoWordsFromProductName(productsDTO.getName());
            Set<ProductsDTO> productSame= findByNameLikeIgnoreCase(name);
            ProductsDTO finalProductsDTO = productsDTO;
            productSame.removeIf(p->p.getProductId().equals(finalProductsDTO.getProductId()));
            if(productSame.size()>4) {
                productSame=productSame.stream().limit(4).collect(Collectors.toSet());
            }
            return Product_findbyid.builder()
                    .product(finalProductsDTO)
                    .productSame(productSame)
                    .build();
        }catch(Exception e){
            throw new ProductException(e.getMessage());
        }
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
//        product.setImage(productsDTO.getImage());
        product.setPrice(productsDTO.getPrice());
        product.setMaterial(productsDTO.getMaterial());
        product.setQuantity(productsDTO.getQuantity());
        Categories category = categoryRepository.findCategoriesByCategoryId(productsDTO.getCategoryId());
        product.setCategory(category);

        // add set colors
//        Set<String> colorIds = productsDTO.getColorNames();
//        Set<Color> colors = new HashSet<>();
//        colorIds.forEach(colorId -> {
//            colors.add(colorRepository.findByColorId(colorId));
//        });
//        product.setColors(colors);


        // add set sizes
//        Set<Integer> sizeIds = productsDTO.getSizeId();
//        Set<Size> sizes = new HashSet<>();
//        sizeIds.forEach(sizeId -> {
//            sizes.add(sizeRepository.findBySizeId(sizeId));
//        });
//        product.setSizes(sizes);
        product.setUpdatedBy(productsDTO.getUpdatedBy());
        return product;
    }

    public ProductsDTO readProduct(Product product,ProductsDTO productsDTO){
        productsDTO.setProductId(product.getProductId());
        productsDTO.setCreatedBy(product.getCreatedBy());
        productsDTO.setCreatedAt(product.getCreatedAt());
        productsDTO.setName(product.getName());
        productsDTO.setCategoryId(product.getCategory().getCategoryId());
        productsDTO.setImage(product.getImage());
        productsDTO.setDescription(product.getDescription());
        productsDTO.setPrice(product.getPrice());
        productsDTO.setQuantity(product.getQuantity());
        productsDTO.setMaterial(product.getMaterial());
        productsDTO.setColorNames(product.getColors());
        Set<Size> sizes = product.getSizes().stream()
                .sorted(Comparator.comparing(Size::getSizeId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<String> sizeNames = new ArrayList<>();
        for (var size : sizes){
            sizeNames.add(size.getName());
        }
        productsDTO.setSizeNames(sizeNames);
        productsDTO.setGalleryImages(product.getGallery_images());
        productsDTO.setUpdatedAt(product.getUpdatedAt());
        productsDTO.setUpdatedBy(product.getUpdatedBy());
        return productsDTO;
    }
    @Override
    public void delete_Product(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Set<ProductsDTO> findALLByLimitOffset(Pageable pageable) {
        try{
            List<Product> products= productRepository.findAll(pageable).getContent();
            Set<ProductsDTO> productsDTOS=new HashSet<>();
            products.forEach((p)->{
                ProductsDTO product=new ProductsDTO();
                productsDTOS.add(readProduct(p,product));
            });
            return productsDTOS;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
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
    public Set<ProductsDTO> findProductFeature() {
        Set<Product> products = productRepository.findAll().stream().limit(4).collect(Collectors.toSet());
        Set<ProductsDTO> productsDTOS = new HashSet<>();
        products.forEach(product -> {
            ProductsDTO productsDTO = new ProductsDTO();
            productsDTO = readProduct(product, productsDTO);
            productsDTOS.add(productsDTO);
        });
        return productsDTOS;
    }

    @Override
    public Set<ProductsDTO> findByNameLikeIgnoreCase(String name) {
        Set<Product> products = productRepository.findByNameLikeIgnoreCase("%" +  name + "%");
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
