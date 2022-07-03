package com.company.repository;


import com.company.entity.ArticleEntity;
import com.company.entity.ArticleLikeEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;


public interface ArticleLikeRepository extends CrudRepository<ArticleLikeEntity, Integer> {



    Optional<ArticleLikeEntity> findByArticleAndProfile(ArticleEntity article, ProfileEntity profile);

    @Query("FROM ArticleLikeEntity a where  a.article.id=:articleId and a.profile.id =:profileId")
    Optional<ArticleLikeEntity> findExists(String articleId, Integer profileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ArticleLikeEntity a where  a.article.id=:articleId and a.profile.id =:profileId")
    void delete(String articleId, Integer profileId);


    Integer countArticleLikeEntityByArticle(ArticleEntity entity);


    //LIKE DIS LAYK COUNT
/*    @Query(value = "select  " +
            "       SUM (CASE WHEN status = 'LIKE' THEN 1 ELSE 0 END) AS like_count, " +
            "       SUM(CASE WHEN status = 'DISLIKE' THEN 1 ELSE 0 END) AS dislike_count " +
            "       from article_like" +
            "       where article_id =:articleId",
            nativeQuery = true)
    Map<String, Integer> countByArticleNative(@Param("articleId") String articleId);*/


    @Query(value = "select  " +
            "         CAST(SUM  (CASE WHEN article_like = 'LIKE' THEN 1 ELSE 0 END) as int) AS like_count ,   " +
            "         CAST(SUM  (CASE WHEN article_like = 'DISLIKE' THEN 1 ELSE 0 END) as int) AS dislike_count " +
            "       from article_like  " +
            "       where article_id =:articleId  ",
            nativeQuery = true)
    Map<String, Integer> countByArticleNative(@Param("articleId") String articleId);

}

