package com.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentFilterDTO {

//    id,created_date_from,created_date_to,profile_id,article_id

    private Integer id;
    private String createdDateFrom;
    private String createdDateTo;
    private String content;
    private Integer profileId;
    private String articleId;


}
