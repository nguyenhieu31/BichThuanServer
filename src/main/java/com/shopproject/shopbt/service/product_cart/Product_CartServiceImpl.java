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
        ProductCartsDTO productCartsDTO = new ProductCartsDTO();
        productCartsDTO.setProductCartId(product_cart.getProduct_cart_id());
        productCartsDTO.setCreateAt(product_cart.getCreateAt());
        productCartsDTO.setUpdateAt(product_cart.getUpdateAt());
        productCartsDTO.setColor(product_cart.getColor());
        productCartsDTO.setQuantity(product_cart.getQuantity());
        productCartsDTO.setSize(product_cart.getSize());
        productCartsDTO.setStatus(product_cart.getStatus());
        productCartsDTO.setCartId(product_cart.getCart().getCartId());
        productCartsDTO.setProductId(product_cart.getProduct().getProductId());

        return productCartsDTO;
    }

    @Override
    public void update_Product_Cart(ProductCartsDTO productCartsDTO) {
        Product_Cart update_product_cart = productCartRepository.findById(productCartsDTO.getProductCartId()).get();

        update_product_cart.setColor(productCartsDTO.getColor());
        update_product_cart.setSize(productCartsDTO.getSize());
        update_product_cart.setQuantity(productCartsDTO.getQuantity());
        update_product_cart.setStatus(productCartsDTO.getStatus());
        Cart cart = cartRepository.findById(productCartsDTO.getCartId()).get();
        update_product_cart.setCart(cart);
        Product product = productRepository.findById(productCartsDTO.getProductId()).get();
        update_product_cart.setProduct(product);


        productCartRepository.save(update_product_cart);
    }

    @Override
    public void delete_Product_CartById(Long id) {
        productCartRepository.deleteById(id);
    }
}
