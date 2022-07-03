package com.company.service;


import com.company.entity.CommentEntity;
import com.company.entity.CommentLikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.CommentLikeStatus;
import com.company.exps.BadRequestException;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.CommentLikeRepository;
import com.company.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private CommentService commentService;

    public void commnetLike(Integer commentId, Integer profileId) {
        likeDislike(commentId, profileId, CommentLikeStatus.LIKE);
    }

    public void commentDisLike(Integer commentId, Integer profileId) {
        likeDislike(commentId, profileId, CommentLikeStatus.DIS_LIKE);
    }

    private void likeDislike(Integer commentId, Integer pId, CommentLikeStatus status) {

        if (!commentService.get(commentId)){
            throw new BadRequestException("visible false");
        }

        Optional<CommentLikeEntity> optional = commentLikeRepository.findExists(commentId, pId);

        if (optional.isPresent()) {
            CommentLikeEntity like = optional.get();
            like.setLikeStatus(status);
            commentLikeRepository.save(like);
            return;
        }
        boolean articleExists = commentRepository.existsById(commentId);
        if (!articleExists) {
            throw new ItemNotFoundEseption("comment NotFound");
        }

        CommentLikeEntity commentLike = new CommentLikeEntity();
        commentLike.setComment(new CommentEntity(commentId));
        commentLike.setProfile(new ProfileEntity(pId));
        commentLike.setLikeStatus(status);
        commentLikeRepository.save(commentLike);
    }

    public void removeLike(Integer commentId, Integer profileId) {
        commentLikeRepository.delete(commentId, profileId);

    }
}
