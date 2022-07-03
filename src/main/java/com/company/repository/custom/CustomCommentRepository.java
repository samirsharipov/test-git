package com.company.repository.custom;


import com.company.dto.CommentFilterDTO;
import com.company.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CustomCommentRepository {

    @Autowired
    private EntityManager entityManager;


    public List<CommentEntity> filter(CommentFilterDTO dto) {

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT a FROM CommentEntity a ");
        builder.append(" where visible=true ");

        if (dto.getId() != null) {
            builder.append(" and a.id = '" + dto.getId() + "' ");
        }


        if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() == null) {
            // builder.append(" and a.publishDate = '" + dto.getPublishedDateFrom() + "' ");
            LocalDate localDate = LocalDate.parse(dto.getCreatedDateFrom());
            LocalDateTime fromTime = LocalDateTime.of(localDate, LocalTime.MIN); // yyyy-MM-dd 00:00:00
            LocalDateTime toTime = LocalDateTime.of(localDate, LocalTime.MAX); // yyyy-MM-dd 23:59:59
            builder.append(" and a.createdDate between '" + fromTime + "' and '" + toTime + "' ");
        } else if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() != null) {
            builder.append(" and a.createdDate between '" + dto.getCreatedDateFrom() + "' and '" + dto.getCreatedDateTo() + "' ");
        }

        if (dto.getContent() != null) {
            builder.append(" and a.content like '%" + dto.getContent() + "%' ");
        }

        if (dto.getArticleId() != null) {
            builder.append("and a.article.id = '" + dto.getArticleId() + "' ");
        }

        if (dto.getProfileId() != null) {
            builder.append("and a.profile.id = '" + dto.getProfileId() + "' ");
        }

        Query query = entityManager.createQuery(builder.toString());
        List<CommentEntity> commentList = query.getResultList();

        return commentList;
    }

}
