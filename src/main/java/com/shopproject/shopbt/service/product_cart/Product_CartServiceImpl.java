package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.entity.Cart;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.entity.Product_Cart;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.product_cart.Product_CartRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class Product_CartServiceImpl implements Product_CartService{
    private Product_CartRepository productCartRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    @Override
    public void create_Product_Cart(ProductCartsDTO productCartsDTO) {
        Product_Cart product_cart = new Product_Cart();
        product_cart = modelMapper.map(productCartsDTO, Product_Cart.class);

        productCartRepository.save(product_cart);
    }

    @Override
    public ProductCartsDTO findProduct_CartById(Long id) {
        Product_Cart product_cart = productCartRepository.findById(id).get();
        ProductCartsDTO productCartsDTO = readProduct_Cart(product_cart, new ProductCartsDTO());

        return productCartsDTO;
    }

    @Override
    public void update_Product_Cart(ProductCartsDTO productCartsDTO) {
        Product_Cart update_product_cart = productCartRepository.findById(productCartsDTO.getProductCartId()).get();
        update_product_cart = readProduct_CartDTO(update_product_cart, productCartsDTO);

        productCartRepository.save(update_product_cart);
    }

    @Override
    public void delete_Product_CartById(Long id) {
        productCartRepository.deleteById(id);
    }

    public ProductCartsDTO readProduct_Cart(Product_Cart product_cart, ProductCartsDTO productCartsDTO){
        productCartsDTO.setProductCartId(product_cart.getProduct_cart_id());
//        productCartsDTO.setCreateAt(product_cart.getCreateAt());
//        productCartsDTO.setUpdateAt(product_cart.getUpdateAt());
        productCartsDTO.setColor(product_cart.getColor());
        productCartsDTO.setQuantity(product_cart.getQuantity());
        productCartsDTO.setSize(product_cart.getSize());
        productCartsDTO.setStatus(product_cart.getStatus());
        productCartsDTO.setCartId(product_cart.getCart().getCartId());
        productCartsDTO.setProductId(product_cart.getProduct().getProductId());

        return productCartsDTO;
    }

    public Product_Cart readProduct_CartDTO(Product_Cart productCart, ProductCartsDTO productCartsDTO){
        productCart.setColor(productCartsDTO.getColor());
        productCart.setSize(productCartsDTO.getSize());
        productCart.setQuantity(productCartsDTO.getQuantity());
        productCart.setStatus(productCartsDTO.getStatus());
        Cart cart = cartRepository.findById(productCartsDTO.getCartId()).get();
        productCart.setCart(cart);
        Product product = productRepository.findById(productCartsDTO.getProductId()).get();
        productCart.setProduct(product);

        return productCart;
    }
    @Override
    public Set<ProductCartsDTO> findProduct_CartByCartId(Long id) {
        Set<Product_Cart> product_carts = productCartRepository.findByCart_CartId(id);
        Set<ProductCartsDTO> productCartsDTOS = new HashSet<>();

        product_carts.forEach(productCart -> {
            ProductCartsDTO productCartsDTO = new ProductCartsDTO();
            productCartsDTO = readProduct_Cart(productCart, productCartsDTO);

            productCartsDTOS.add(productCartsDTO);
        });
        return productCartsDTOS;
    }
}
