package com.company.repository;


import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SavedArticleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SavedArticleRepository extends CrudRepository<SavedArticleEntity, Integer> {

        Optional<SavedArticleEntity> findByIdAndAndProfile(Integer integer, ProfileEntity entity);

        boolean existsByArticleAndProfile(ArticleEntity entity, ProfileEntity profileEntity);

        List<SavedArticleEntity> findAllByProfileId(Integer id);

}
