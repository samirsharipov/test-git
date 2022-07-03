package com.company.repository;


import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTypeRepository extends CrudRepository<ArticleTypeEntity, Integer> {
    List<ArticleTypeEntity> findAllByArticle(ArticleEntity entity);
/*
    @Query(value = "select * from article_types order by id limit :limit offset :offset ;", nativeQuery = true)
    List<ArticleTypeEntity> pagination(@Param("limit") int limit, @Param("offset") int offset );

    @Query("SELECT a.article FROM ArticleTypeEntity a where a.types.key =:key order by a.article.publishDate")
    List<ArticleEntity> findTop5ByArticle(@Param("key") String key);*/

    @Query(value = "SELECT art.* " +
            " FROM article_type as a " +
            " inner join article as art on art.id = a.article_id " +
            " inner join type as t on t.id = a.types_id " +
            " Where  t.key =:key  " +
            " order by art.publish_date " +
            " limit 5",
            nativeQuery = true)
    List<ArticleEntity> findTop5ByArticleNative(@Param("key") String key);

}
