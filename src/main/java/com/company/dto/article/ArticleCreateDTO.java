package com.company.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCreateDTO {


    @NotNull
    @Size(min = 10, max = 255, message = "About Me must be between 10 and 255 characters")
    private String title;

    @NotNull(message = "mazgi title qani")
    private String content;
    @NotBlank(message = "qani descreption")
    private String description;

    @NotBlank(message = "requerid region")
    private Integer regionId;

    @NotBlank(message = "requerid category")
    private Integer categoryId;

    @NotNull
    private String imageId;

    private List<Integer> typesList;
    private List<String> tagList;

}
