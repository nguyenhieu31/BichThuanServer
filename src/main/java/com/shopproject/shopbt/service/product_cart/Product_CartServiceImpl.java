package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.product_cart.Product_CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.AddToCartRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class Product_CartServiceImpl implements Product_CartService{
    private Product_CartRepository productCartRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    @Override
    public String create_Product_Cart(Long id, AddToCartRequest request) throws LoginException, ProductException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserDetails user= (UserDetails) authentication.getPrincipal();
            Optional<User> findUser= userRepository.findByUserName(user.getUsername());
            if(findUser.isPresent()){
                Optional<Product> product= productRepository.findById(id);
                Cart findCartByUserId= cartRepository.findByUser_Userid(findUser.get().getUserid());
                if (product.isPresent() && findCartByUserId !=null){
                        Optional<Product_Cart> isProductCart= productCartRepository.findByProduct_ProductIdAndSize(id,request.getSize());
                    try{
                        if(isProductCart.isPresent()){
                            Product_Cart existingProductCart = isProductCart.get();
                            existingProductCart.setQuantity(existingProductCart.getQuantity() + request.getQuantity());
                            productCartRepository.save(existingProductCart);
                        }else{
                            var productCart= Product_Cart.builder()
                                    .product(product.get())
                                    .cart(findCartByUserId)
                                    .color(request.getColor())
                                    .size(request.getSize())
                                    .quantity(request.getQuantity())
                                    .status(0)
                                    .build();
                            productCartRepository.save(productCart);
                        }
                        return "thêm vào giỏ hàng thành công";
                    }catch(Exception e){
                        throw new ProductException("Không thể thêm sản phẩm vào giỏ hàng");
                    }
                }else{
                    throw new ProductException("không tìm thấy sản phẩm");
                }
            }else{
                throw new LoginException("không tìm thấy người dùng");
            }

        }else{
            throw new LoginException("bạn chưa đăng nhập?");
        }
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
