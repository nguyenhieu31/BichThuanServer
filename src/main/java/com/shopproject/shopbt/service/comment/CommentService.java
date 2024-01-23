package com.shopproject.shopbt.service.comment;

import com.shopproject.shopbt.dto.CommentsDTO;
import com.shopproject.shopbt.entity.Comment;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.CommentRequest;

import java.util.Set;

public interface CommentService {
    Comment create_Comment(CommentRequest commentRequest, User user) throws Exception;
    Set<Comment> getAllReviewedProduct(User user) throws Exception;
    CommentsDTO findCommentById(Long id);

    void update_Comment(CommentsDTO commentsDTO);

    void delete_CommentById(Long id);
}
