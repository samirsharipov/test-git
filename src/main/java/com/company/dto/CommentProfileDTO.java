package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter

public class CommentProfileDTO {

    private Integer id;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    // id,created_date,update_date,profile(id,name,surname)

    private ProfileByCommentDTO profile;




}
