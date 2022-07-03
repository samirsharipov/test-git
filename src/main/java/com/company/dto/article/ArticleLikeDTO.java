package com.company.dto.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikeDTO {


    private Integer likeCount = 0;
    private Integer dislikeCount = 0;
    private String articleId;


}
