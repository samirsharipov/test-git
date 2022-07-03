package com.company.repository;


import com.company.entity.ArticleEntity;
import com.company.entity.ArticleTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleTagRepository extends CrudRepository<ArticleTagEntity, Integer> {

    List<ArticleTagEntity> findAllByArticle(ArticleEntity article);
}
