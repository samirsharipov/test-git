package com.company.service;


import com.company.dto.article.ArticleLikeDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ArticleLikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.exps.ItemNotFoundEseption;
import com.company.repository.ArticleLikeRepository;
import com.company.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ArticleLikeService {
    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    @Autowired
    private ArticleRepository articleRepository;



    public void articleLike(String articleId, Integer pId) {
        likeDislike(articleId, pId, LikeStatus.LIKE);
    }

    public void articleDisLike(String articleId, Integer pId) {
        likeDislike(articleId, pId, LikeStatus.DIS_LIKE);
    }

    private void likeDislike(String articleId, Integer pId, LikeStatus status) {

        Optional<ArticleLikeEntity> optional = articleLikeRepository.findExists(articleId, pId);
        if (optional.isPresent()) {
            ArticleLikeEntity like = optional.get();
            like.setStatus(status);
            articleLikeRepository.save(like);
            return;
        }
        boolean articleExists = articleRepository.existsById(articleId);
        if (!articleExists) {
            throw new ItemNotFoundEseption("Article NotFound");
        }

        ArticleLikeEntity like = new ArticleLikeEntity();
        like.setArticle(new ArticleEntity(articleId));
        like.setProfile(new ProfileEntity(pId));
        like.setStatus(status);
        articleLikeRepository.save(like);
    }

    public void removeLike(String articleId, Integer pId) {
       /* Optional<ArticleLikeEntity> optional = articleLikeRepository.findExists(articleId, pId);
        optional.ifPresent(articleLikeEntity -> {
            articleLikeRepository.delete(articleLikeEntity);
        });*/
        articleLikeRepository.delete(articleId, pId);
    }

    public Integer likeCount(String articleId) {
        return articleLikeRepository.countArticleLikeEntityByArticle(new ArticleEntity(articleId));
    }


    public ArticleLikeDTO likeCountAndDislikeCount(String articleId) {
//        int like = articleLikeRepository.countByArticleAndStatus(new ArticleEntity(articleId), LikeStatus.LIKE);
//        int dislike = articleLikeRepository.countByArticle(articleId, LikeStatus.DISLIKE);

        Map<String, Integer> map = articleLikeRepository.countByArticleNative(articleId);

        ArticleLikeDTO dto = new ArticleLikeDTO();
        dto.setLikeCount(map.get("like_count"));
        dto.setDislikeCount(map.get("dislike_count"));
        return dto;
    }
}