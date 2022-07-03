package com.company.dto;


import com.company.enums.CommentLikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommetLikeDTO {

    private CommentLikeStatus commetLikeStatus;
    private Integer commentId;


}
