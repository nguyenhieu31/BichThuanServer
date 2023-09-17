package com.shopproject.shopbt.service.comment;

import com.shopproject.shopbt.dto.CommentsDTO;

public interface CommentService {
    void create_Comment(CommentsDTO commentsDTO);

    CommentsDTO findCommentById(Long id);

    void update_Comment(CommentsDTO commentsDTO);

    void delete_CommentById(Long id);
}
