package com.shopproject.shopbt.repository.product_cart;

import com.shopproject.shopbt.entity.Product_Cart;
import com.shopproject.shopbt.response.CartResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface Product_CartRepository extends JpaRepository<Product_Cart, Long> {
    @Query("select new com.shopproject.shopbt.response.CartResponse(pc.product_cart_id,p.productId,p.name,pc.color,pc.size,p.price,p.image,pc.quantity)  from Product_Cart pc " +
            "inner join Cart c on pc.cart.cartId=c.cartId " +
            "inner join Product p on pc.product.productId=p.productId " +
            "where pc.cart.cartId=:cartId")
    List<CartResponse> findByCart_CartId(@Param("cartId") Long cartId);
//    @Query("select pc from Product_Cart pc where pc.product.productId=:productId and pc.size=:size and pc.cart.cartId=:cartId")
//    Optional<Product_Cart> findByProduct_ProductIdAndSizeAndCart_CartId(@Param("productId") Long productId,@Param("size") String size,@Param("cartId") Long cartId);
    @Query("SELECT pc FROM Product_Cart pc " +
            "INNER JOIN FETCH pc.product p " + // JOIN FETCH để tải dữ liệu sản phẩm
            "INNER JOIN pc.cart c " +
            "WHERE p.productId = :productId AND pc.size = :size AND c.cartId = :cartId")
    Optional<Product_Cart> findByProduct_ProductIdAndSizeAndCart_CartId(@Param("productId") Long productId, @Param("size") String size, @Param("cartId") Long cartId);


    @Query("select pc from Product_Cart pc where pc.product_cart_id=:productCartId")
    Product_Cart findByProduct_cart_id(@Param("productCartId") Long productCartId);
}