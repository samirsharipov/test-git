package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDTO {

    @NotBlank(message = "requerid name")
    private String name;

    @NotBlank(message = "requerid surname")
    private String surname;

    @Email
    private String email;

    @NotBlank(message = "requerid passwor")
    @Size(min = 10)
    private String password;

    @NotNull
    private String photoId;


}
