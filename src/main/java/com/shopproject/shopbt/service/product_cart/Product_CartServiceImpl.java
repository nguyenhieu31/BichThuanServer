package com.shopproject.shopbt.service.product_cart;

import com.shopproject.shopbt.ExceptionCustom.ProductException;
import com.shopproject.shopbt.dto.ProductCartsDTO;
import com.shopproject.shopbt.entity.*;
import com.shopproject.shopbt.repository.carts.CartRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.product_cart.Product_CartRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.AddToCartRequest;
import com.shopproject.shopbt.request.CartRequest;
import com.shopproject.shopbt.request.ProductCartRequest;
import com.shopproject.shopbt.response.CartResponse;
import com.shopproject.shopbt.service.Redis.RedisService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class Product_CartServiceImpl implements Product_CartService{
    private final Product_CartRepository productCartRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    @Value("${GOOGLE.STATE_KEY}")
    private String googleState;
    @Override
    public String create_Product_Cart(Long id, AddToCartRequest request) throws LoginException, ProductException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User user= (User) authentication.getPrincipal();
            Product product= productRepository.findByProductId(id);
            Cart findCartByUserId= cartRepository.findByUser_Userid(user.getUserid());
            if (product.getProductId()!=null && findCartByUserId !=null){
                Optional<Product_Cart> isProductCart= productCartRepository.findByProduct_ProductIdAndSizeAndCart_CartId(id,request.getSize(),findCartByUserId.getCartId());
                try{
                    if(isProductCart.isPresent()){
                        if(!(isProductCart.get().getQuantity()==product.getQuantity())){
                            Product_Cart existingProductCart = isProductCart.get();
                            existingProductCart.setQuantity(existingProductCart.getQuantity() + request.getQuantity());
                            productCartRepository.save(existingProductCart);
                        }else{
                            throw new ProductException("Không thể thêm sản phẩm quá số lượng trong kho đang có");
                        }
                        Product_Cart existingProductCart = isProductCart.get();
                        existingProductCart.setQuantity(existingProductCart.getQuantity() + request.getQuantity());
                        productCartRepository.save(existingProductCart);
                    }else{
                        var productCart= Product_Cart.builder()
                                .product(product)
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
            throw new LoginException("bạn chưa đăng nhập?");
        }
    }

    @Override
    public List<CartResponse> getAllProductCartByUser() throws LoginException,ClassCastException {
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                Cart getCartByUserId= cartRepository.findByUser_Userid(user.getUserid());
                return findProduct_CartByCartId(getCartByUserId.getCartId());
            }else{
                throw new LoginException("bạn chưa đăng nhập?");
            }
        }catch(ClassCastException e){
            throw new ClassCastException("bạn chưa đăng nhập?");
        }
    }

    @Override
    public ProductCartsDTO findProduct_CartById(Long id) throws ProductException {
        Product_Cart productCart = productCartRepository.findByProduct_cart_id(id);
        if(productCart!=null){
            Product product= productRepository.findByProductId(productCart.getProduct().getProductId());
            int quantityNew= productCart.getQuantity()+1;
            if(product!=null && quantityNew<product.getQuantity()){
                productCart.setQuantity(quantityNew);
                productCartRepository.save(productCart);
            }else{
                throw new ProductException("không thể thêm sản phẩm vượt quá số lượng trong kho!");
            }
        }
        assert productCart != null;
        return readProduct_Cart(productCart, new ProductCartsDTO());
    }

    @Override
    public CartResponse incrementProductCart(Long productCartId, ProductCartRequest request) throws Exception {
        try{
            Product product= productRepository.findByProductId(request.getProductId());
            if(product!=null && product.getQuantity()>request.getQuantity()){
                    Product_Cart productCart=productCartRepository.findByProduct_cart_id(productCartId);
                    int newQuantity= request.getQuantity()+1;
                    productCart.setQuantity(newQuantity);
                    productCartRepository.save(productCart);
                    return CartResponse.builder()
                            .productCartId(productCartId)
                            .quantity(newQuantity)
                            .size(request.getSize())
                            .color(request.getColor())
                            .image(request.getImage())
                            .name(request.getName())
                            .productId(request.getProductId())
                            .price(request.getPrice())
                            .build();
            }else{
                throw new Exception("không thể thêm vì vượt quá số lượng trong kho");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public CartResponse decrementProductCart(Long productCartId, ProductCartRequest request) throws Exception {
        try{
            Product_Cart productCart= productCartRepository.findByProduct_cart_id(productCartId);
            if (productCart!=null){
                int newQuantity= request.getQuantity()-1;
                productCart.setQuantity(newQuantity);
                productCartRepository.save(productCart);
                return CartResponse.builder()
                        .productCartId(productCartId)
                        .quantity(newQuantity)
                        .size(request.getSize())
                        .color(request.getColor())
                        .image(request.getImage())
                        .name(request.getName())
                        .productId(request.getProductId())
                        .price(request.getPrice())
                        .build();
            }else{
                throw new Exception("không thể giảm số lượng sản phẩm trong giỏ");
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void update_Product_Cart(ProductCartsDTO productCartsDTO) {
        Product_Cart update_product_cart = productCartRepository.findById(productCartsDTO.getProductCartId()).get();
        update_product_cart = readProduct_CartDTO(update_product_cart, productCartsDTO);

        productCartRepository.save(update_product_cart);
    }

//    @Override
//    public CartResponse updateQuantity(CartRequest cartRequest) throws Exception {
//        try{
//            Optional<Product_Cart> productCartOP= productCartRepository.findById(cartRequest.getProductCartId());
//            if(productCartOP.isPresent()){
//                Product_Cart productCart= productCartOP.get();
//                if(productCart.getProduct().getQuantity()>cartRequest.getQuantity()){
//                    productCart.setQuantity(cartRequest.getQuantity());
//                    productCartRepository.save(productCart);
//                    return CartResponse.builder()
//                            .productCartId(productCart.getProduct_cart_id())
//                            .productId(productCart.getProduct().getProductId())
//                            .price(productCart.getProduct().getPrice())
//                            .name(productCart.getProduct().getName())
//                            .image(productCart.getProduct().getImage())
//                            .color(productCart.getColor())
//                            .size(productCart.getSize())
//                            .quantity(productCart.getQuantity())
//                            .build();
//                }else{
//                    throw new Exception("so luong khong phu hop");
//                }
//            }else{
//                throw new Exception("khong the cap nhat so luong");
//            }
//        }catch (Exception e){
//            throw new Exception("khong tim thay san pham");
//        }
//    }

    @Override
    public void delete_Product_CartById(Long productCartId) {
        productCartRepository.deleteById(productCartId);
    }

    @Override
    public List<CartResponse> updateCart(String userName) throws Exception {
        try{
            if(userName!=null){
                Optional<User> findUserByUserName= userRepository.findByUserName(userName);
                if(findUserByUserName.isPresent()){
                    Cart findCartByUserId= cartRepository.findByUser_Userid(findUserByUserName.get().getUserid());
                    return findProduct_CartByCartId(findCartByUserId.getCartId());
                }else{
                    throw new LoginException("người dùng chưa đăng nhập");
                }
            }else{
                throw new LoginException("phien dang nhap het han");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
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
    private List<CartResponse> findProduct_CartByCartId(Long id) {
        return productCartRepository.findByCart_CartId(id);
    }
}
