package com.shopproject.shopbt.service.comment;

import com.shopproject.shopbt.dto.CommentsDTO;
import com.shopproject.shopbt.entity.Color;
import com.shopproject.shopbt.entity.Comment;
import com.shopproject.shopbt.entity.Product;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.repository.color.ColorRepository;
import com.shopproject.shopbt.repository.comment.CommentRepository;
import com.shopproject.shopbt.repository.product.ProductRepository;
import com.shopproject.shopbt.repository.size.SizeRepository;
import com.shopproject.shopbt.repository.user.UserRepository;
import com.shopproject.shopbt.request.CommentRequest;
import com.shopproject.shopbt.response.RatingCommentResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;


    @Override
    public Comment create_Comment(CommentRequest commentRequest, User user) throws Exception {
        try{
            Comment isCommented= commentRepository.findByCommentByProductIdAndUserId(commentRequest.getProductId(), user.getUserid()).orElse(null);
            if(isCommented==null){
                Product product = productRepository.findById(commentRequest.getProductId()).orElse(null);
                if (product != null) {
                    Comment commentCreate = Comment.builder()
                            .descriptionFeature(commentRequest.getDescriptionFeature())
                            .descriptionProductQuality(commentRequest.getDescriptionProductQuality())
                            .user(user)
                            .product(product)
                            .rating(commentRequest.getRating())
                            .color(commentRequest.getColor())
                            .size(commentRequest.getSize())
                            .userName(commentRequest.isShowUserName() ? user.getUsername() : "Anonymous")
                            .isActive(false)
                            .build();
                    return commentRepository.save(commentCreate);
                } else {
                    throw new Exception("Không tìm thấy sản phẩm!");
                }
            }else{
                throw new Exception("Bạn đã gửi đánh giá!");
            }

        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Set<Comment> getAllReviewedProduct(User user) throws Exception {
        try{
            return commentRepository.findCommentByUser(user.getUserid());
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Comment> findCommentByProduct(Long productId) throws Exception {
        try{
            return commentRepository.findCommentByProduct(productId);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Set<RatingCommentResponse> getAllRatingByProduct(Long productId) throws Exception {
        try{
            return commentRepository.findRatingCommentByProduct(productId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
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
