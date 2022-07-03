package com.company.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleUpdateDTO {


    @NotNull
    @Size(min = 10, max = 255, message = "About Me must be between 10 and 255 characters")
    private String title;

    @NotBlank(message = "requerid content")
    @Size(min = 10, max = 255, message = "About Me must be between 10 and 255 characters")
    private String content;

    @NotBlank(message = "qani descreption")
    private String description;

    @NotNull
    private String imageId;



}
