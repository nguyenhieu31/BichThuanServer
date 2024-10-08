package com.shopproject.shopbt.controller.web;

import com.shopproject.shopbt.ExceptionCustom.LoginException;
import com.shopproject.shopbt.entity.Comment;
import com.shopproject.shopbt.entity.User;
import com.shopproject.shopbt.request.CommentRequest;
import com.shopproject.shopbt.response.CommentResponse;
import com.shopproject.shopbt.response.RatingCommentResponse;
import com.shopproject.shopbt.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/web/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                Comment response= commentService.create_Comment(commentRequest,user);
                return ResponseEntity.status(HttpStatus.OK).body(CommentResponse.builder()
                                .commentId(response.getCommentId())
                                .size(response.getSize())
                                .color(response.getColor())
                                .userName(response.getUserName())
                                .productId(response.getProduct().getProductId())
                                .nameProduct(response.getProduct().getName())
                                .descriptionFeature(response.getDescriptionFeature())
                                .descriptionProductQuality(response.getDescriptionProductQuality())
                                .isActive(response.isActive())
                        .build());
            }else{
                throw new LoginException("Phiên đăng nhập hết hạn");
            }
        }catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("/reviewed-product")
    @Transactional
    public ResponseEntity<?> getAllReviewedProduct(){
        try{
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(!(authentication instanceof AnonymousAuthenticationToken)){
                User user= (User) authentication.getPrincipal();
                Set<Comment> response=commentService.getAllReviewedProduct(user);
                Set<CommentResponse> commentResponses= new HashSet<>();
                response.forEach(comment -> {
                    var commentRes= CommentResponse.builder()
                            .commentId(comment.getCommentId())
                            .productId(comment.getProduct().getProductId())
                            .descriptionFeature(comment.getDescriptionFeature())
                            .descriptionProductQuality(comment.getDescriptionProductQuality())
                            .size(comment.getSize())
                            .color(comment.getColor())
                            .userName(comment.getUserName())
                            .nameProduct(comment.getProduct().getName())
                            .isActive(comment.isActive())
                            .build();
                    commentResponses.add(commentRes);
                });
                return ResponseEntity.status(HttpStatus.OK).body(commentResponses);
            }else{
                throw new LoginException("Phiên đăng nhập hết hạn");
            }
        }catch (LoginException e){
            return ResponseEntity.status(403).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/product")
    public ResponseEntity<?> findCommentByUserAndProduct(@RequestParam("productId") Long productId){
        try{
            List<Comment> commentResponse= commentService.findCommentByProduct(productId);
            List<CommentResponse> comments= new ArrayList<>();
            commentResponse.forEach(comment -> {
                var commentResult= CommentResponse.builder()
                        .commentId(comment.getCommentId())
                        .productId(comment.getProduct().getProductId())
                        .descriptionProductQuality(comment.getDescriptionProductQuality())
                        .descriptionFeature(comment.getDescriptionFeature())
                        .size(comment.getSize())
                        .color(comment.getColor())
                        .userName(comment.getUserName())
                        .rating(comment.getRating())
                        .isActive(comment.isActive())
                        .nameProduct(comment.getProduct().getName())
                        .createdAt(comment.getCreatedAt())
                        .build();
                comments.add(commentResult);
            });
            return ResponseEntity.status(HttpStatus.OK).body(comments);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/rating")
    public ResponseEntity<?> getAllRatingProduct(@RequestParam("productId") Long productId){
        try{
            Set<RatingCommentResponse> ratingCommentResponses= commentService.getAllRatingByProduct(productId);
            return ResponseEntity.status(HttpStatus.OK).body(ratingCommentResponses);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
