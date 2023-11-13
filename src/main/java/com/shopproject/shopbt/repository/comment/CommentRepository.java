package com.shopproject.shopbt.repository.comment;

import com.shopproject.shopbt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
