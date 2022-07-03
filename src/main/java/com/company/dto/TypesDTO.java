package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypesDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 20)
    private String key;

    @NotBlank
    @Size(min = 3, max = 20)
    private String nameUz;

    @NotBlank
    @Size(min = 3, max = 20)
    private String nameRu;

    @NotBlank
    @Size(min = 3, max = 20)
    private String nameEn;
    @NotNull
    private Boolean visible;

    private String name;
}
