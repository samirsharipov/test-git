package com.company.dto;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {

    private Integer id;

    @NotNull
    @Size(min = 4, max = 10, message = "About Me must be between 10 and 255 characters")
    private String name;

    @NotNull
    @Size(min = 4, max = 10, message = "About Me must be between 10 and 255 characters")
    private String surname;

    @Email
    private String email;

    @NotNull
    private ProfileRole role;

    @Size(min = 5)
    private String password;


    private String photoId;

    private String jwt;

}
