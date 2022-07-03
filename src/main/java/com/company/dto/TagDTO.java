package com.company.dto;


import com.company.enums.TagStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {

    private Integer id;
    private String name;
    private TagStatus status;
    private LocalDateTime createdDate;

}
