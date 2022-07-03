package com.company.dto;

import com.company.dto.article.ArticleCommentDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter

public class CommentProfileArticleDTO {

    private Integer id;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String content;
    private Integer replaseId;

    private ProfileByCommentDTO profile;
    private ArticleCommentDTO article;



}
