package com.company.dto.article;


import com.company.dto.*;
import com.company.enums.ArticleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {

    private String id;
    private String title;
    private String content;
    private String description;

    private RegionDTO region;
    private CategoryDTO category;

    private LocalDateTime createdDate;
    private LocalDateTime publishDate;

    private AttachDTO attachDTO;

    private Integer viewCount;
    private Integer sharedCount;
    private Integer likeCount;

    private List<TypesDTO> typesList;
    private List<TagDTO> tagList;

    private ArticleStatus status;
    private Boolean visible = Boolean.TRUE;

    private ArticleLikeDTO like;

}
