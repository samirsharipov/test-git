package com.company.mapper;

import java.time.LocalDateTime;

public interface CommentShortInfoByArticlePagination {

    Integer getId();
    LocalDateTime getCreatedDate();
    LocalDateTime getUpdateDate();
    Integer getprofileId();
    String getName();
    String getSurname();
    String getContent();
    Integer getReplaseId();
    String getArticleId();
    String getTitle();
}
