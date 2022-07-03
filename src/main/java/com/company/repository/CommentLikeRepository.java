package com.company.repository;

import com.company.entity.CommentEntity;
import com.company.entity.CommentLikeEntity;
import com.company.entity.ProfileEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {

    Optional<CommentLikeEntity> findByCommentAndProfile(CommentEntity comment, ProfileEntity profile);

    @Query("FROM CommentLikeEntity c where  c.comment.id=:commentId and c.profile.id =:profileId")
    Optional<CommentLikeEntity> findExists(Integer commentId, Integer profileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommentLikeEntity c where  c.comment.id=:commentId and c.profile.id =:profileId")
    void delete(Integer commentId, Integer profileId);



}
