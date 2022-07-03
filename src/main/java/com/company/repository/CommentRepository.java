package com.company.repository;


import com.company.entity.CommentEntity;
import com.company.mapper.CommentShortInfoByArticle;
import com.company.mapper.CommentShortInfoByArticlePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {


    Optional<CommentEntity> findByIdAndVisibleTrue(Integer comId);

    @Query(value = "SELECT  c.id as id, c.created_date as createdDate, c.update_date as updeteDate, p.id as profileId, p.name as name, p.surname as surname" +
            "  FROM profile as p" +
            "  inner join comment as c on c.profile_id = p.id  " +
            "  Where c.article_id =:id  and  c.visible = true  ",
              nativeQuery = true)
    List<CommentShortInfoByArticle> getArticleCommentListByArticleId(@Param("id") String id);


    @Query(value = "SELECT  c.id as id, c.created_date as createdDate, c.update_date as updeteDate, p.id as profileId, p.name as name, p.surname as surname, c.content as content, c.replase_id as replaseId, a.id as articleId, a.title as title" +
            "  FROM comment as c" +
            "  inner join profile as p on c.profile_id = p.id  " +
            "  inner join article as a on c.article_id = a.id  " +
            "  Where   c.visible = true  ",
             nativeQuery = true)
    Page<CommentShortInfoByArticlePagination> commentListPagination(Pageable pageable);


}
