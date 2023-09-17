package com.shopproject.shopbt.service.comment;

import com.shopproject.shopbt.dto.CommentsDTO;
import com.shopproject.shopbt.entity.Comment;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.comment.CommentRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;


    @Override
    public void create_Comment(CommentsDTO commentsDTO) {
        Comment comment = modelMapper.map(commentsDTO, Comment.class);
//        User user = userRepository.findById(commentsDTO.getUserId()).get();
//        comment.setUser(user);

        commentRepository.save(comment);
    }

    @Override
    public CommentsDTO findCommentById(Long id) {
        return modelMapper.map(commentRepository.findById(id).get(), CommentsDTO.class);
    }

    @Override
    public void update_Comment(CommentsDTO commentsDTO) {
        Comment comment = commentRepository.findById(commentsDTO.getCommentId()).get();
        comment = modelMapper.map(commentsDTO, Comment.class);

        commentRepository.save(comment);
    }

    @Override
    public void delete_CommentById(Long id) {
        commentRepository.deleteById(id);
    }
}
