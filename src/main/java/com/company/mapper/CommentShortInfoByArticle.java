package com.company.mapper;

import java.time.LocalDateTime;

public interface CommentShortInfoByArticle {

    Integer getId();
    LocalDateTime getCreatedDate();
    LocalDateTime getUpdateDate();
    Integer getprofileId();
    String getName();
    String getSurname();
// id,created_date,update_date,profile(id,name,surname)
}
