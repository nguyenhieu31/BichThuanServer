package com.shopproject.shopbt.repository.comment;

import com.shopproject.shopbt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.product.productId=:productId and c.user.userid=:userId")
    Optional<Comment> findByCommentByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);
    @Query("select c from Comment c where c.user.userid=:userId")
    Set<Comment> findCommentByUser(@Param("userId") Long userId);
}
